package com.alltobs.desensitization.annotation;

import com.alltobs.desensitization.enums.DesensitizeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解 DesensitizeField controller 方法的注解，指定方法的返回值是否脱敏
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DesensitizeField {
    Field[] fields();

    @interface Field {
        String name();

        DesensitizeType type() default DesensitizeType.CUSTOM;

        /**
         * 脱敏字符，默认是 "*"
         */
        String maskChar() default "*";

        /**
         * 功能开关，若为 true，则返回的数据不包含该字段
         */
        boolean exclude() default false;
    }
}
