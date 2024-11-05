package com.alltobs.desensitization.handler;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.annotation.ValidateDesensitize;
import com.alltobs.desensitization.annotation.ValidateDesensitizes;
import com.alltobs.desensitization.enums.DesensitizeType;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 类 DesensitizationRequestBodyAdvice 请求体处理器
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@ControllerAdvice
public class DesensitizationRequestBodyAdvice implements RequestBodyAdvice {

    private static final ThreadLocal<Map<String, ValidateDesensitize>> METHOD_FIELD_CONFIG = new ThreadLocal<>();

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

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
            throws IOException {
        return inputMessage; // 在读取请求体之前不做处理
    }

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

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter methodParameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body; // 对于空的请求体不做处理
    }

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

    private boolean isDesensitized(String value, DesensitizeType type, String maskChar) {
        if (maskChar == null || maskChar.isEmpty()) {
            maskChar = "*"; // 默认脱敏字符
        }

        // 转义脱敏字符，以便在正则表达式中使用
        String escapedMaskChar = Pattern.quote(maskChar);

        switch (type != null ? type : DesensitizeType.CUSTOM) {
            case MOBILE_PHONE:
                String mobileRegex = "^\\d{3}" + escapedMaskChar + "{4}\\d{4}$";
                return value.matches(mobileRegex);
            case EMAIL:
                int atIndex = value.indexOf("@");
                if (atIndex <= 0) {
                    return false; // 非法的邮箱格式
                }
                String localPart = value.substring(0, atIndex);
                String emailRegex = ".*" + escapedMaskChar + "+.*";
                return localPart.matches(emailRegex);
            case ID_CARD:
                String idCardRegex = "^\\d{3}" + escapedMaskChar + "{10}[0-9Xx]{4}$";
                return value.matches(idCardRegex);
            case CUSTOM:
            default:
                String customRegex = ".*" + escapedMaskChar + "+.*";
                return value.matches(customRegex);
        }
    }

    // 判断是否为基本类型或包装类型
    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() ||
                type == Integer.class || type == Long.class ||
                type == Double.class || type == Float.class ||
                type == Boolean.class || type == Character.class ||
                type == Byte.class || type == Short.class;
    }
}
