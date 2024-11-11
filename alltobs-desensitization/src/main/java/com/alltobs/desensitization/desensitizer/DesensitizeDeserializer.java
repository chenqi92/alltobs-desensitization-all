package com.alltobs.desensitization.desensitizer;

import com.alltobs.desensitization.annotation.JsonDesensitize;
import com.alltobs.desensitization.serializer.Desensitizer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 自定义反序列化器，用于在反序列化时过滤敏感数据。
 *
 * @author ChenQi
 * &#064;date 2024/11/11
 */
public class DesensitizeDeserializer extends JsonDeserializer<String> implements ContextualDeserializer {

    private Class<? extends Desensitizer> desensitizerClass;
    private String maskChar;
    private boolean ignoreDesensitized;

    public DesensitizeDeserializer() {
    }

    public DesensitizeDeserializer(Class<? extends Desensitizer> desensitizerClass, String maskChar, boolean ignoreDesensitized) {
        this.desensitizerClass = desensitizerClass;
        this.maskChar = maskChar;
        this.ignoreDesensitized = ignoreDesensitized;
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();

        if (value == null || value.isEmpty()) {
            if (ignoreDesensitized) {
                // 忽略该字段，不进行设置
                return null;
            } else {
                return value;
            }
        }

        try {
            Desensitizer desensitizer = desensitizerClass.getDeclaredConstructor().newInstance();

            if (ignoreDesensitized) {
                String regex = desensitizer.getDesensitizedRegex(maskChar);
                Pattern pattern = Pattern.compile(regex);
                if (pattern.matcher(value).matches()) {
                    // 匹配脱敏后的格式，忽略该字段
                    return null;
                }
            }

            // 返回原始值
            return value;
        } catch (Exception e) {
            throw new IOException("Failed to instantiate desensitizer", e);
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        if (property != null) {
            JsonDesensitize annotation = property.getAnnotation(JsonDesensitize.class);
            if (annotation == null) {
                annotation = property.getContextAnnotation(JsonDesensitize.class);
            }
            if (annotation != null) {
                return new DesensitizeDeserializer(annotation.type(), annotation.maskChar(), annotation.ignoreDesensitized());
            }
        }
        return this;
    }
}
