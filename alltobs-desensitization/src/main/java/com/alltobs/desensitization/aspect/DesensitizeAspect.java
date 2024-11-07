package com.alltobs.desensitization.aspect;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.annotation.Desensitizes;
import com.alltobs.desensitization.utils.DesensitizeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 脱敏切面，拦截带有 {@link Desensitizes} 注解的方法，进行脱敏处理。
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Aspect
@Component
public class DesensitizeAspect {

    /**
     * 方法执行前进行脱敏，针对字段脱敏
     */
    @Before("@annotation(desensitizes)")
    public void desensitizeFields(JoinPoint joinPoint, Desensitizes desensitizes) throws IllegalAccessException {
        // 获取方法参数
        Object[] args = joinPoint.getArgs();

        // 遍历所有的脱敏字段注解
        for (Desensitize desensitize : desensitizes.value()) {
            String fieldName = desensitize.field();
            String maskChar = desensitize.maskChar();

            // 遍历方法参数
            for (Object arg : args) {
                if (arg != null) {
                    Class<?> argClass = arg.getClass();
                    try {
                        Field field = argClass.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object fieldValue = field.get(arg);

                        // 如果字段值不为空，则进行脱敏
                        if (fieldValue != null) {
                            String desensitizedValue = DesensitizeUtils.applyDesensitize(desensitize.type(), fieldValue.toString(), maskChar);
                            field.set(arg, desensitizedValue);
                        }
                    } catch (NoSuchFieldException e) {
                        // 如果找不到该字段，忽略此错误
                        continue;
                    }
                }
            }
        }
    }

    /**
     * 方法返回后进行脱敏，针对返回值的字段进行脱敏
     */
    @AfterReturning(value = "@annotation(desensitizes)", returning = "result")
    public Object desensitizeReturnValue(JoinPoint joinPoint, Desensitizes desensitizes, Object result) throws IllegalAccessException {
        if (result != null) {
            // 遍历脱敏字段
            for (Desensitize desensitize : desensitizes.value()) {
                String fieldName = desensitize.field();
                String maskChar = desensitize.maskChar();

                // 如果字段存在于返回值中，则进行脱敏
                Class<?> resultClass = result.getClass();
                try {
                    Field field = resultClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object fieldValue = field.get(result);

                    // 如果字段值不为空，则进行脱敏
                    if (fieldValue != null) {
                        String desensitizedValue = DesensitizeUtils.applyDesensitize(desensitize.type(), fieldValue.toString(), maskChar);
                        field.set(result, desensitizedValue);
                    }
                } catch (NoSuchFieldException e) {
                    // 如果找不到字段，跳过
                    continue;
                }
            }
        }
        return result;
    }
}
