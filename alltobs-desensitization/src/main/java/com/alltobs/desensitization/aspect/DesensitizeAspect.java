package com.alltobs.desensitization.aspect;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.enums.DesensitizeType;
import com.alltobs.desensitization.utils.DesensitizeUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 类 DesensitizeAspect
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Aspect
public class DesensitizeAspect {

    /**
     * 拦截被 @Desensitize 注解的方法
     *
     * @param joinPoint   切点
     * @param desensitize 注解
     * @return Object
     * @throws Throwable 异常
     */
    @Around("@annotation(desensitize)")
    public Object aroundDesensitizeMethod(ProceedingJoinPoint joinPoint, Desensitize desensitize) throws Throwable {
        Object result = joinPoint.proceed();

        if (desensitize != null) {
            result = processDesensitization(result, desensitize);
        }

        return result;
    }

    /**
     * 拦截被 @RestController 或 @Controller 注解的方法
     *
     * @param joinPoint 切点
     * @return Object
     * @throws Throwable 异常
     */
    @Around("execution(* *(..)) && (@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)) && !@annotation(com.alltobs.desensitization.annotation.Desensitize)")
    public Object aroundControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        // 处理字段级别的脱敏
        if (result != null) {
            processFieldDesensitization(result);
        }

        return result;
    }

    /**
     * 处理脱敏
     *
     * @param result      结果
     * @param desensitize 注解
     * @return Object
     * @throws IllegalAccessException 异常
     */
    private Object processDesensitization(Object result, Desensitize desensitize) throws IllegalAccessException {
        if (result == null) {
            return null;
        }

        if (isPrimitiveOrWrapper(result.getClass()) || result instanceof String) {
            return result;
        }

        if (result instanceof Collection) {
            for (Object item : (Collection<?>) result) {
                desensitizeObject(item, desensitize);
            }
        } else {
            desensitizeObject(result, desensitize);
        }

        return result;
    }

    /**
     * 处理字段脱敏
     *
     * @param result 结果
     * @throws IllegalAccessException 异常
     */
    private void processFieldDesensitization(Object result) throws IllegalAccessException {
        if (result == null) {
            return;
        }

        if (isPrimitiveOrWrapper(result.getClass()) || result instanceof String) {
            return;
        }

        if (result instanceof Collection) {
            for (Object item : (Collection<?>) result) {
                desensitizeObjectFields(item);
            }
        } else {
            desensitizeObjectFields(result);
        }
    }

    /**
     * 对象脱敏
     *
     * @param obj         对象
     * @param desensitize 注解
     * @throws IllegalAccessException 异常
     */
    private void desensitizeObject(Object obj, Desensitize desensitize) throws IllegalAccessException {
        if (obj == null) return;

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Map<String, Desensitize.Field> fieldConfigMap = new HashMap<>();
        for (Desensitize.Field fieldConfig : desensitize.fields()) {
            fieldConfigMap.put(fieldConfig.name(), fieldConfig);
        }

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            String fieldName = field.getName();

            DesensitizeType type = desensitize.type();
            String maskChar = desensitize.maskChar();
            boolean exclude = desensitize.exclude();

            // 1. 优先使用方法级别的字段配置
            if (fieldConfigMap.containsKey(fieldName)) {
                Desensitize.Field fieldConfig = fieldConfigMap.get(fieldName);

                DesensitizeType fieldType = fieldConfig.type() != DesensitizeType.DEFAULT ? fieldConfig.type() : type;
                String fieldMaskChar = !fieldConfig.maskChar().isEmpty() ? fieldConfig.maskChar() : maskChar;
                boolean fieldExclude = fieldConfig.exclude();

                applyDesensitization(field, obj, value, fieldType, fieldMaskChar, fieldExclude);
            }
            // 2. 然后处理字段上的 @Desensitize 注解
            else if (field.isAnnotationPresent(Desensitize.class)) {
                Desensitize fieldDesensitize = field.getAnnotation(Desensitize.class);
                applyDesensitization(field, obj, value, fieldDesensitize.type(), fieldDesensitize.maskChar(), fieldDesensitize.exclude());
            }
            // 3. 可选的，自动脱敏
            else {
                // 可根据需要启用自动脱敏
//                String maskedValue = DesensitizeUtils.autoDesensitize(value);
//                field.set(obj, maskedValue);
            }
        }
    }

    // 根据字段上的注解进行对象脱敏
    private void desensitizeObjectFields(Object obj) throws IllegalAccessException {
        if (obj == null) return;

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (field.isAnnotationPresent(Desensitize.class)) {
                Desensitize fieldDesensitize = field.getAnnotation(Desensitize.class);
                applyDesensitization(field, obj, value, fieldDesensitize.type(), fieldDesensitize.maskChar(), fieldDesensitize.exclude());
            } else {
                // 可根据需要启用自动脱敏
//                String maskedValue = DesensitizeUtils.autoDesensitize(value);
//                field.set(obj, maskedValue);
            }
        }
    }

    // 应用脱敏处理
    private void applyDesensitization(Field field, Object obj, Object value, DesensitizeType type, String maskChar, boolean exclude) throws IllegalAccessException {
        if (exclude) {
            field.set(obj, null);
        } else {
            String maskedValue = DesensitizeUtils.desensitize(value, type, maskChar);
            field.set(obj, maskedValue);
        }
    }

    // 判断是否为基本类型或包装类型
    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() || type == Integer.class || type == Long.class || type == Double.class || type == Float.class || type == Boolean.class || type == Character.class || type == Byte.class || type == Short.class;
    }
}
