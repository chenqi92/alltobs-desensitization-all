package com.alltobs.desensitization.annotation;

import java.lang.annotation.*;

/**
 * 方法级别的脱敏注解，允许对多个字段进行脱敏配置。
 *
 * @author ChenQi
 * &#064;date 2024/11/4
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitizes {

    /**
     * 包含的脱敏配置数组。
     */
    Desensitize[] value() default {};
}
