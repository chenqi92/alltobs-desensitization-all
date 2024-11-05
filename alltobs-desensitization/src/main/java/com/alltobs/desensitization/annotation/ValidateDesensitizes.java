package com.alltobs.desensitization.annotation;

import java.lang.annotation.*;

/**
 * 方法级别的校验脱敏注解，允许对多个字段进行脱敏校验配置。
 *
 * @author ChenQi
 * &#064;date 2024/11/4
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateDesensitizes {

    /**
     * 包含的校验脱敏配置数组。
     */
    ValidateDesensitize[] value() default {};
}
