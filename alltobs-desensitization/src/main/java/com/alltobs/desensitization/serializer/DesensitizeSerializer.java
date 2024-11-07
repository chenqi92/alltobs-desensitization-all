package com.alltobs.desensitization.serializer;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.utils.DesensitizeUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
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

    private Desensitize desensitize;

    public DesensitizeSerializer() {
    }

    public DesensitizeSerializer(Desensitize desensitize) {
        this.desensitize = desensitize;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        if (desensitize != null) {
            String strValue = value != null ? value.toString() : "";
            String maskedValue = DesensitizeUtils.applyDesensitize(
                    desensitize.type(), strValue, desensitize.maskChar());
            gen.writeString(maskedValue);
        } else {
            gen.writeObject(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov,
                                              BeanProperty property) {
        if (property != null) {
            Desensitize desensitize = property.getAnnotation(Desensitize.class);
            if (desensitize != null) {
                return new DesensitizeSerializer(desensitize);
            }
        }
        return this;
    }
}
