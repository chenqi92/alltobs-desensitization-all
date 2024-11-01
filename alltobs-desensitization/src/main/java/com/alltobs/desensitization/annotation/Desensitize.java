package com.alltobs.desensitization.annotation;

import com.alltobs.desensitization.enums.DesensitizeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解 Desensitize 字段注解
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Desensitize {

    /**
     * 全局脱敏类型，默认为 CUSTOM
     */
    DesensitizeType type() default DesensitizeType.CUSTOM;

    /**
     * 全局脱敏字符，默认是 "*"
     */
    String maskChar() default "*";

    /**
     * 全局功能开关，若为 true，则返回的数据不包含该字段
     */
    boolean exclude() default false;

    /**
     * 需要脱敏的字段列表
     */
    Field[] fields() default {};

    @interface Field {
        String name();

        DesensitizeType type() default DesensitizeType.DEFAULT;

        String maskChar() default "";

        boolean exclude() default false;
    }
}
