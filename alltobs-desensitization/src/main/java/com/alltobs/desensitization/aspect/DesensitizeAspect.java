package com.alltobs.desensitization.aspect;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.annotation.Desensitizes;
import com.alltobs.desensitization.utils.DesensitizeUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 脱敏切面，拦截带有 {@link Desensitizes} 注解的方法，进行脱敏处理。
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Aspect
@Component
public class DesensitizeAspect {

    // 定义切入点，扫描所有包含 @Desensitize 和 @Desensitizes 注解的方法
    @Pointcut("execution(* com.alltobs..*.*(..))")
    public void desensitizePointcut() {
    }

    @Around("desensitizePointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object result = joinPoint.proceed();

        // 处理方法参数中的 @Desensitize 和 @Desensitizes 注解
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg != null) {
                // 处理 @Desensitize 注解
                processDesensitizeOnFields(arg);

                // 处理 @Desensitizes 注解
                processDesensitizesOnMethod(arg, joinPoint);
            }
        }

        // 处理返回值中的 @Desensitize 和 @Desensitizes 注解
        if (result != null) {
            processDesensitizeOnFields(result);
            processDesensitizesOnMethod(result, joinPoint);
        }

        return result;
    }

    // 处理字段上的 @Desensitize 注解
    private void processDesensitizeOnFields(Object target) throws IllegalAccessException {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            Desensitize desensitize = field.getAnnotation(Desensitize.class);
            if (desensitize != null) {
                field.setAccessible(true);
                Object fieldValue = field.get(target);

                if (desensitize.exclude()) {
                    field.set(target, null); // 如果 exclude 为 true，设置字段为 null
                } else {
                    if (fieldValue != null) {
                        String desensitizedValue = DesensitizeUtils.applyDesensitize(
                                desensitize.type(), fieldValue.toString(), desensitize.maskChar());
                        field.set(target, desensitizedValue);
                    }
                }
            }
        }
    }

    // 处理方法上的 @Desensitizes 注解
    private void processDesensitizesOnMethod(Object target, ProceedingJoinPoint joinPoint) throws IllegalAccessException, NoSuchMethodException {
        Method method = joinPoint.getSignature().getDeclaringType().getMethod(joinPoint.getSignature().getName(), getParameterTypes(joinPoint.getArgs()));
        Desensitizes desensitizes = method.getAnnotation(Desensitizes.class);
        if (desensitizes != null) {
            // 遍历 @Desensitize 注解进行处理
            Arrays.stream(desensitizes.value()).forEach(desensitize -> {
                try {
                    String fieldName = desensitize.field();
                    Field field = target.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object fieldValue = field.get(target);

                    if (desensitize.exclude()) {
                        field.set(target, null); // 如果 exclude 为 true，设置字段为 null
                    } else {
                        if (fieldValue != null) {
                            String desensitizedValue = DesensitizeUtils.applyDesensitize(
                                    desensitize.type(), fieldValue.toString(), desensitize.maskChar());
                            field.set(target, desensitizedValue);
                        }
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // 获取方法的参数类型
    private Class<?>[] getParameterTypes(Object[] args) {
        return Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
    }
}
