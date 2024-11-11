package com.alltobs.desensitization.annotation;

import com.alltobs.desensitization.desensitizer.DefaultDesensitizer;
import com.alltobs.desensitization.desensitizer.DesensitizeDeserializer;
import com.alltobs.desensitization.serializer.DesensitizeSerializer;
import com.alltobs.desensitization.serializer.Desensitizer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * 用于在序列化和反序列化时处理敏感数据
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizeSerializer.class)
@JsonDeserialize(using = DesensitizeDeserializer.class)
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

    /**
     * 是否在反序列化时忽略脱敏后的数据。
     * 如果设置为 true，且前端传入的值为 null 或匹配脱敏正则表达式，则忽略该字段。
     */
    boolean ignoreDesensitized() default false;
}
