package com.alltobs.desensitization.handler;

import com.alltobs.desensitization.exception.DesensitizationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * 类 DesensitizationRequestBodyAdvice 请求体处理器
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@ControllerAdvice
public class DesensitizationRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true; // 应用于所有请求体
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage; // 读取前不做处理
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            validateFields(body);
        } catch (IllegalAccessException e) {
            throw new DesensitizationException("字段校验失败", e);
        }
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body; // 空请求体不做处理
    }

    private void validateFields(Object obj) throws IllegalAccessException {
        if (obj == null) return;
        Class<?> clazz = obj.getClass();
        if (clazz.getName().startsWith("java.")) {
            // 跳过 Java 内置类
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null) continue;
            if (value instanceof String && ((String) value).contains("*")) {
                // 字段包含脱敏字符，抛出异常
                throw new DesensitizationException("字段 " + field.getName() + " 包含脱敏字符");
            } else if (!field.getType().isPrimitive()) {
                // 递归检查嵌套对象
                validateFields(value);
            }
        }
    }
}
