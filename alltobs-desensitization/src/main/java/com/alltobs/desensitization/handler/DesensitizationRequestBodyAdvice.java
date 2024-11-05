package com.alltobs.desensitization.handler;

import com.alltobs.desensitization.annotation.ValidateDesensitize;
import com.alltobs.desensitization.annotation.ValidateDesensitizes;
import com.alltobs.desensitization.enums.DesensitizeType;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理和请求体脱敏处理类。
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@ControllerAdvice
public class DesensitizationRequestBodyAdvice implements RequestBodyAdvice {

    private static final ThreadLocal<Map<String, ValidateDesensitize>> METHOD_FIELD_CONFIG = new ThreadLocal<>();

    /**
     * 支持指定方法中的字段脱敏配置。
     *
     * @param methodParameter 方法参数
     * @param targetType      目标类型
     * @param converterType   转换器类型
     * @return 是否支持脱敏
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        ValidateDesensitizes validateMethod = methodParameter.getMethodAnnotation(ValidateDesensitizes.class);
        if (validateMethod != null) {
            Map<String, ValidateDesensitize> fieldConfigMap = new HashMap<>();
            for (ValidateDesensitize fieldConfig : validateMethod.value()) {
                fieldConfigMap.put(fieldConfig.field(), fieldConfig);
            }
            METHOD_FIELD_CONFIG.set(fieldConfigMap);
            return true;
        }
        return false;
    }

    /**
     * 处理请求体数据之前不做任何修改。
     *
     * @param inputMessage    请求消息
     * @param methodParameter 方法参数
     * @param targetType      目标类型
     * @param converterType   转换器类型
     * @return 原始请求消息
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
            throws IOException {
        return inputMessage; // 不进行处理
    }

    /**
     * 处理请求体数据并进行字段脱敏。
     *
     * @param body            请求体内容
     * @param inputMessage    请求消息
     * @param methodParameter 方法参数
     * @param targetType      目标类型
     * @param converterType   转换器类型
     * @return 脱敏后的请求体内容
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter methodParameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            Map<String, ValidateDesensitize> fieldConfigMap = METHOD_FIELD_CONFIG.get();
            validateFields(body, fieldConfigMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // 记录异常
        } finally {
            METHOD_FIELD_CONFIG.remove();
        }
        return body;
    }

    /**
     * 处理空请求体数据，不做任何修改。
     *
     * @param body            请求体内容
     * @param inputMessage    请求消息
     * @param methodParameter 方法参数
     * @param targetType      目标类型
     * @param converterType   转换器类型
     * @return 原始请求体内容
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter methodParameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body; // 不做处理
    }

    /**
     * 校验字段是否需要脱敏。
     *
     * @param obj            对象
     * @param fieldConfigMap 字段配置映射
     * @throws IllegalAccessException 反射异常
     */
    private void validateFields(Object obj, Map<String, ValidateDesensitize> fieldConfigMap) throws IllegalAccessException {
        if (obj == null || fieldConfigMap == null) return;

        Class<?> clazz = obj.getClass();
        if (clazz.getName().startsWith("java.")) {
            return; // 跳过 Java 内置类
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null) continue;

            ValidateDesensitize fieldConfig = fieldConfigMap.get(field.getName());

            if (fieldConfig != null) {
                String maskChar = fieldConfig.maskChar();
                DesensitizeType type = fieldConfig.type();

                // 检查字段值是否包含脱敏内容
                if (isDesensitized(value.toString(), type, maskChar)) {
                    System.out.println("字段 " + field.getName() + " 包含脱敏内容，已被忽略。");
                    // 将字段设置为 null，表示忽略该字段
                    field.set(obj, null);
                    continue; // 处理下一个字段
                }
            }

            // 递归检查嵌套对象
            if (!isPrimitiveOrWrapper(field.getType()) && !(value instanceof String)) {
                validateFields(value, fieldConfigMap);
            }
        }
    }

    /**
     * 判断字段类型是否是原始类型或者包装类。
     *
     * @param type 字段类型
     * @return 是否是原始类型或包装类
     */
    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() ||
                type == Integer.class ||
                type == Long.class ||
                type == Double.class ||
                type == Boolean.class ||
                type == Character.class ||
                type == Byte.class ||
                type == Short.class ||
                type == Float.class;
    }

    /**
     * 检查字符串是否需要脱敏处理。
     *
     * @param value    字符串值
     * @param type     脱敏类型
     * @param maskChar 脱敏字符
     * @return 是否需要脱敏
     */
    private boolean isDesensitized(String value, DesensitizeType type, String maskChar) {
        // 根据脱敏类型和字段值决定是否脱敏
        return value != null && !value.isEmpty(); // TODO: 按脱敏规则校验
    }
}
