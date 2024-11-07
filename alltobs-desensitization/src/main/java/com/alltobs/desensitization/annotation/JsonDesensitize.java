package com.alltobs.desensitization.annotation;

import com.alltobs.desensitization.enums.DesensitizeType;

import java.lang.annotation.*;

/**
 * 注解 JsonDesensitize
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonDesensitize {

    /**
     * 脱敏类型（例如：手机号、身份证等）
     */
    DesensitizeType type() default DesensitizeType.DEFAULT;

    /**
     * 脱敏字符（例如：*、#等）
     */
    String maskChar() default "*";

    /**
     * 指定脱敏作用的字段（当作用于方法时，可以指定方法参数的字段）
     */
    String field() default "";

    /**
     * 是否排除字段，排除时直接不返回该字段
     */
    boolean exclude() default false;
}
