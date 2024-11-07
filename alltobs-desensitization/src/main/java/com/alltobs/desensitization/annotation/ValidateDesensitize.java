package com.alltobs.desensitization.annotation;

import com.alltobs.desensitization.enums.DesensitizeType;

import java.lang.annotation.*;

/**
 * 校验脱敏注解，应用于字段或参数，用于方法校验时进行字段脱敏。
 *
 * @author ChenQi
 * &#064;date 2024/11/4
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateDesensitize {

    /**
     * 字段名称（用于方法配置）。
     */
    String field() default "";

    /**
     * 脱敏类型，默认是自定义脱敏。
     */
    DesensitizeType type() default DesensitizeType.DEFAULT;

    /**
     * 脱敏时使用的掩码字符，默认是“*”。
     */
    String maskChar() default "*";

    /**
     * 是否启用脱敏功能（仅用于字段注解），默认启用。
     */
    boolean enabled() default true;
}
