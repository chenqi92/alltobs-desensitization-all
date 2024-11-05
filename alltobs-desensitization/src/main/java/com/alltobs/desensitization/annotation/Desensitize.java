package com.alltobs.desensitization.annotation;

import com.alltobs.desensitization.enums.DesensitizeType;

import java.lang.annotation.*;

/**
 * 注解 Desensitize 字段注解
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitize {
    // 字段名称（用于方法配置）
    String field() default "";

    // 脱敏类型
    DesensitizeType type() default DesensitizeType.CUSTOM;

    // 掩码字符
    String maskChar() default "*";

    // 是否启用（仅用于字段注解）
    boolean enabled() default false;
}
