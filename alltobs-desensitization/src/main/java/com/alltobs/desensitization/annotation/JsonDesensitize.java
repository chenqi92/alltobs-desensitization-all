package com.alltobs.desensitization.annotation;

import com.alltobs.desensitization.desensitizer.DefaultDesensitizer;
import com.alltobs.desensitization.enums.DesensitizeType;
import com.alltobs.desensitization.serializer.Desensitizer;

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
     * 脱敏器类型，指定具体的脱敏器类，必须实现 Desensitizer 接口。
     * 默认为 DefaultDesensitizer。
     */
    Class<? extends Desensitizer> type() default DefaultDesensitizer.class;

    /**
     * 脱敏字符（例如：*、#等）
     */
    String maskChar() default "*";

    /**
     * 是否排除字段，排除时直接不返回该字段
     */
    boolean exclude() default false;
}
