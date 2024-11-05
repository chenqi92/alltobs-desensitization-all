package com.alltobs.desensitization.aspect;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.annotation.Desensitizes;
import com.alltobs.desensitization.enums.DesensitizeType;
import com.alltobs.desensitization.serializer.DesensitizeSerializerModifier;
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

    @Around("@annotation(desensitizes)")
    public Object around(ProceedingJoinPoint joinPoint, Desensitizes desensitizes) throws Throwable {
        Map<String, Desensitize> fieldConfigMap = new HashMap<>();
        for (Desensitize desensitize : desensitizes.value()) {
            fieldConfigMap.put(desensitize.field(), desensitize);
        }
        // 设置方法级别的字段脱敏配置
        DesensitizeSerializerModifier.setMethodFieldConfig(fieldConfigMap);
        try {
            return joinPoint.proceed();
        } finally {
            // 清除线程本地变量
            DesensitizeSerializerModifier.clearMethodFieldConfig();
        }
    }
}
