package com.alltobs.desensitization.serializer;

import com.alltobs.desensitization.annotation.JsonDesensitize;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

/**
 * 类 DesensitizeSerializer
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public class DesensitizeSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private Class<? extends Desensitizer> desensitizerClass;
    private String maskChar;

    public DesensitizeSerializer() {
    }

    public DesensitizeSerializer(Class<? extends Desensitizer> desensitizerClass, String maskChar) {
        this.desensitizerClass = desensitizerClass;
        this.maskChar = maskChar;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        String strValue = value.toString();
        Desensitizer desensitizer = getDesensitizerInstance(desensitizerClass);
        if (desensitizer != null) {
            String maskedValue = desensitizer.desensitize(strValue, maskChar);
            gen.writeString(maskedValue);
        } else {
            // 未找到对应的脱敏器，使用原值
            gen.writeString(strValue);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
            throws JsonMappingException {
        if (property != null) {
            JsonDesensitize annotation = property.getAnnotation(JsonDesensitize.class);
            if (annotation == null) {
                annotation = property.getContextAnnotation(JsonDesensitize.class);
            }
            if (annotation != null) {
                return new DesensitizeSerializer(annotation.type(), annotation.maskChar());
            }
        }
        return this;
    }

    private Desensitizer getDesensitizerInstance(Class<? extends Desensitizer> desensitizerClass) {
        try {
            if (desensitizerClass != null) {
                return desensitizerClass.getDeclaredConstructor().newInstance();
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
