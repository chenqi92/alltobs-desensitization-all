package com.alltobs.desensitization.annotation;

import java.lang.annotation.*;

/**
 * 注解 Desensitizes
 *
 * @author ChenQi
 * &#064;date 2024/11/4
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitizes {
    Desensitize[] value() default {};
}
