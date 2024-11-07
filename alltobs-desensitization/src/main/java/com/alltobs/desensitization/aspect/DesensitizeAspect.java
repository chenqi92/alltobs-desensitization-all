package com.alltobs.desensitization.aspect;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.annotation.Desensitizes;
import com.alltobs.desensitization.utils.DesensitizeUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

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
     * 处理 @Desensitize 注解
     */
    @Before("@annotation(desensitizes)")
    public void desensitizeFields(JoinPoint joinPoint, Desensitizes desensitizes) throws IllegalAccessException {
        Object[] args = joinPoint.getArgs();
        for (Desensitize desensitize : desensitizes.value()) {
            String fieldName = desensitize.field();
            String maskChar = desensitize.maskChar();

            for (Object arg : args) {
                if (arg != null) {
                    Class<?> argClass = arg.getClass();
                    try {
                        Field field = argClass.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object fieldValue = field.get(arg);

                        if (fieldValue != null) {
                            String desensitizedValue = DesensitizeUtils.applyDesensitize(desensitize.type(), fieldValue.toString(), maskChar);
                            field.set(arg, desensitizedValue);
                        }
                    } catch (NoSuchFieldException ignored) {
                    }
                }
            }
        }
    }

    /**
     * 方法返回后进行脱敏，针对返回值的字段进行脱敏
     * 处理 @Desensitize 和 @Desensitizes 注解
     */
    @AfterReturning(value = "@annotation(desensitizes)", returning = "result")
    public Object desensitizeReturnValue(JoinPoint joinPoint, Desensitizes desensitizes, Object result) throws IllegalAccessException {
        if (result != null) {
            // 处理方法上的 @Desensitizes 注解
            for (Desensitize desensitize : desensitizes.value()) {
                String fieldName = desensitize.field();
                String maskChar = desensitize.maskChar();

                // 如果需要排除字段
                if (desensitize.exclude()) {
                    removeField(result, fieldName);
                } else {
                    Class<?> resultClass = result.getClass();
                    try {
                        Field field = resultClass.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object fieldValue = field.get(result);

                        if (fieldValue != null) {
                            String desensitizedValue = DesensitizeUtils.applyDesensitize(desensitize.type(), fieldValue.toString(), maskChar);
                            field.set(result, desensitizedValue);
                        }
                    } catch (NoSuchFieldException ignored) {
                    }
                }
            }

            // 处理返回值对象中字段的 @Desensitize 注解
            for (Field field : result.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Desensitize.class)) {
                    Desensitize fieldDesensitize = field.getAnnotation(Desensitize.class);
                    if (fieldDesensitize != null) {
                        String fieldName = field.getName();
                        String maskChar = fieldDesensitize.maskChar();

                        if (fieldDesensitize.exclude()) {
                            removeField(result, fieldName);
                        } else {
                            field.setAccessible(true);
                            Object fieldValue = field.get(result);
                            if (fieldValue != null) {
                                String desensitizedValue = DesensitizeUtils.applyDesensitize(fieldDesensitize.type(), fieldValue.toString(), maskChar);
                                field.set(result, desensitizedValue);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 移除返回值中的指定字段
     */
    private void removeField(Object result, String fieldName) throws IllegalAccessException {
        try {
            Field field = result.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(result, null);
        } catch (NoSuchFieldException e) {
            // 如果找不到字段，跳过
        }
    }
}
