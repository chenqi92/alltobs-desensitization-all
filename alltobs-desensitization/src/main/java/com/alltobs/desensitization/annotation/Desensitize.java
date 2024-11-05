package com.alltobs.desensitization.annotation;

import com.alltobs.desensitization.enums.DesensitizeType;

import java.lang.annotation.*;

/**
 * 脱敏注解，应用于字段或参数，用于标记需要进行脱敏的字段。
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitize {

    /**
     * 字段名称（用于方法配置），如果为空，则表示使用默认字段名。
     */
    String field() default "";

    /**
     * 脱敏类型，默认是自定义脱敏。
     */
    DesensitizeType type() default DesensitizeType.CUSTOM;

    /**
     * 脱敏时使用的掩码字符，默认是“*”。
     */
    String maskChar() default "*";

    /**
     * 是否启用脱敏功能（仅用于字段注解），默认启用。
     */
    boolean enabled() default true;
}
