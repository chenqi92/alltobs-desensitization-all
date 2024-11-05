package com.alltobs.desensitization.aspect;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.annotation.Desensitizes;
import com.alltobs.desensitization.enums.DesensitizeType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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
     * 切点，拦截所有标注了 @Desensitize 注解的字段
     */
    @Pointcut("@annotation(com.alltobs.desensitization.annotation.Desensitize)")
    public void desensitizeMethods() {
    }

    /**
     * 前置处理方法入参
     * 处理入参中的脱敏字段
     *
     * @param joinPoint 切点
     */
    @Before("execution(* com.alltobs..*(..)) && @annotation(com.alltobs.desensitization.annotation.Desensitize)")
    public void handleMethodParameters(JoinPoint joinPoint) {
        // 获取方法入参
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg != null) {
                // 反射获取字段并进行脱敏
                desensitizeFields(arg);
            }
        }
    }

    /**
     * 后置处理返回值
     * 处理返回值中的脱敏字段
     *
     * @param returnValue 方法返回值
     */
    @AfterReturning(value = "execution(* com.alltobs..*(..)) && @annotation(com.alltobs.desensitization.annotation.Desensitize)", returning = "returnValue")
    public void handleMethodReturnValue(Object returnValue) {
        if (returnValue != null) {
            // 反射获取字段并进行脱敏
            desensitizeFields(returnValue);
        }
    }

    /**
     * 处理对象中的字段进行脱敏
     *
     * @param object 目标对象
     */
    private void desensitizeFields(Object object) {
        // 获取类中的所有字段
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            // 获取字段上的脱敏注解
            Desensitize desensitize = field.getAnnotation(Desensitize.class);
            if (desensitize != null) {
                try {
                    // 获取字段的值
                    Object value = field.get(object);

                    // 如果字段有值，则进行脱敏处理
                    if (value != null) {
                        String desensitizedValue = applyDesensitize(desensitize, value.toString());
                        // 设置脱敏后的值回字段
                        field.set(object, desensitizedValue);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据脱敏配置类型进行脱敏操作
     *
     * @param desensitize 脱敏配置
     * @param value       字段的值
     * @return 脱敏后的值
     */
    private String applyDesensitize(Desensitize desensitize, String value) {
        String maskChar = desensitize.maskChar();
        DesensitizeType type = desensitize.type();

        switch (type) {
            case MOBILE_PHONE:
                return desensitizeMobilePhone(value, maskChar);
            case EMAIL:
                return desensitizeEmail(value, maskChar);
            case ID_CARD:
                return desensitizeIdCard(value, maskChar);
            default:
                return desensitizeCustom(value, maskChar);
        }
    }

    private String desensitizeMobilePhone(String value, String maskChar) {
        // 实现手机号脱敏逻辑
        return value.substring(0, 3) + maskChar.repeat(4) + value.substring(7);
    }

    private String desensitizeEmail(String value, String maskChar) {
        // 实现邮箱脱敏逻辑
        int atIndex = value.indexOf("@");
        return value.charAt(0) + maskChar.repeat(atIndex - 1) + value.substring(atIndex);
    }

    private String desensitizeIdCard(String value, String maskChar) {
        // 实现身份证号脱敏逻辑
        return value.substring(0, 6) + maskChar.repeat(value.length() - 10) + value.substring(value.length() - 4);
    }

    private String desensitizeCustom(String value, String maskChar) {
        // 默认的脱敏逻辑
        return maskChar.repeat(value.length());
    }
}
