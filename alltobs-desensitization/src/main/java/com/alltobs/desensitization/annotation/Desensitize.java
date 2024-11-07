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
