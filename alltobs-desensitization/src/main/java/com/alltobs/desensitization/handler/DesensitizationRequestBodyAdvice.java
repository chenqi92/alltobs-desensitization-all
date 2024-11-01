package com.alltobs.desensitization.handler;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.enums.DesensitizeType;
import com.alltobs.desensitization.exception.DesensitizationException;
import com.alltobs.desensitization.utils.DesensitizeUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

/**
 * 类 DesensitizationRequestBodyAdvice 请求体处理器
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@ControllerAdvice
public class DesensitizationRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true; // 应用于所有请求体
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
            throws IOException {
        return inputMessage; // 读取前不做处理
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter methodParameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            validateFields(body);
        } catch (IllegalAccessException e) {
            // 记录异常日志，或进行其他处理
            e.printStackTrace();
        }
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter methodParameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body; // 空请求体不做处理
    }

    private void validateFields(Object obj) throws IllegalAccessException {
        if (obj == null) return;
        Class<?> clazz = obj.getClass();
        if (clazz.getName().startsWith("java.")) {
            return; // 跳过 Java 内置类
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null) continue;

            // 获取字段上的 @Desensitize 注解
            Desensitize desensitize = field.getAnnotation(Desensitize.class);

            if (desensitize != null) {
                String maskChar = desensitize.maskChar();
                DesensitizeType type = desensitize.type();

                // 检测字段值是否包含脱敏内容
                if (isDesensitized(value.toString(), type, maskChar)) {
                    // 将字段设置为 null，表示不接受该字段
                    field.set(obj, null);
                    continue; // 处理下一个字段
                }
            }

            // 如果字段是对象或集合，递归检查
            if (!isPrimitiveOrWrapper(field.getType()) && !(value instanceof String)) {
                validateFields(value);
            }
        }
    }

    private boolean isDesensitized(String value, DesensitizeType type, String maskChar) {
        if (maskChar == null || maskChar.isEmpty()) {
            maskChar = "*"; // 默认脱敏字符
        }

        // 转义脱敏字符
        String escapedMaskChar = Pattern.quote(maskChar);

        switch (type != null ? type : DesensitizeType.CUSTOM) {
            case MOBILE_PHONE:
                // 检测手机号是否被脱敏（中间四位为脱敏字符）
                String mobileRegex = "^\\d{3}" + escapedMaskChar + "{4}\\d{4}$";
                return value.matches(mobileRegex);
            case EMAIL:
                // 检测邮箱是否被脱敏（本地部分包含脱敏字符）
                int atIndex = value.indexOf("@");
                if (atIndex <= 0) {
                    return false; // 非法的邮箱格式
                }
                String localPart = value.substring(0, atIndex);
                String emailRegex = ".*" + escapedMaskChar + "+.*";
                return localPart.matches(emailRegex);
            case ID_CARD:
                // 检测身份证号是否被脱敏（中间部分为脱敏字符）
                String idCardRegex = "^\\d{3}" + escapedMaskChar + "{10}[0-9Xx]{4}$";
                return value.matches(idCardRegex);
            case CUSTOM:
            default:
                // 自定义类型，可以根据需要调整
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
