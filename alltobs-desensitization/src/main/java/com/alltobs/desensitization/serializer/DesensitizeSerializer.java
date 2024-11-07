package com.alltobs.desensitization.serializer;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.annotation.JsonDesensitize;
import com.alltobs.desensitization.utils.DesensitizeUtils;
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

    private JsonDesensitize desensitize;

    public DesensitizeSerializer() {
    }

    public DesensitizeSerializer(JsonDesensitize desensitize) {
        this.desensitize = desensitize;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        if (desensitize != null) {
            if (desensitize.exclude()) {
                // 排除字段
                return;
            } else {
                String strValue = value != null ? value.toString() : "";
                Desensitizer desensitizer = getDesensitizerInstance(desensitize.type());
                if (desensitizer != null) {
                    String maskedValue = desensitizer.desensitize(strValue, desensitize.maskChar());
                    gen.writeString(maskedValue);
                } else {
                    // 未找到对应的脱敏器，使用原值
                    gen.writeString(strValue);
                }
            }
        } else {
            gen.writeObject(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov,
                                              BeanProperty property) throws JsonMappingException {
        if (property != null) {
            JsonDesensitize desensitize = property.getAnnotation(JsonDesensitize.class);
            if (desensitize != null) {
                return new DesensitizeSerializer(desensitize);
            }
        }
        return this;
    }

    private Desensitizer getDesensitizerInstance(Class<? extends Desensitizer> desensitizerClass) {
        try {
            // 反射创建实例
            return desensitizerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            // 处理异常
            return null;
        }
    }
}
