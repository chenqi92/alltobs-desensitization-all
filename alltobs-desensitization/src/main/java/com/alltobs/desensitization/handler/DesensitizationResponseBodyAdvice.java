package com.alltobs.desensitization.handler;

import com.alltobs.desensitization.annotation.Desensitizes;
import com.alltobs.desensitization.context.DesensitizationContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * 类 DesensitizationResponseBodyAdvice
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
@Component
public class DesensitizationResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 检查方法是否有 @Desensitizes 注解
        return returnType.hasMethodAnnotation(Desensitizes.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  org.springframework.http.MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  org.springframework.http.server.ServerHttpRequest request,
                                  org.springframework.http.server.ServerHttpResponse response) {
        // 获取方法上的 @Desensitizes 注解
        Method method = returnType.getMethod();
        Desensitizes desensitizes = method.getAnnotation(Desensitizes.class);

        if (desensitizes != null) {
            // 将脱敏规则存储到上下文中
            DesensitizationContextHolder.setDesensitizationRules(desensitizes.value());
        } else {
            // 清除上下文，防止污染
            DesensitizationContextHolder.clear();
        }

        return body;
    }
}
