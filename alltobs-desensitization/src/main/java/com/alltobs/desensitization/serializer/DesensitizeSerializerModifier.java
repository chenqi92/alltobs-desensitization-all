package com.alltobs.desensitization.serializer;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.enums.DesensitizeType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.List;
import java.util.Map;

/**
 * 类 DesensitizeSerializerModifier
 *
 * @author ChenQi
 * &#064;date 2024/11/5
 */
public class DesensitizeSerializerModifier extends BeanSerializerModifier {

    private static final ThreadLocal<Map<String, Desensitize>> METHOD_FIELD_CONFIG = new ThreadLocal<>();

    public static void setMethodFieldConfig(Map<String, Desensitize> config) {
        METHOD_FIELD_CONFIG.set(config);
    }

    public static void clearMethodFieldConfig() {
        METHOD_FIELD_CONFIG.remove();
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        Map<String, Desensitize> methodFieldConfig = METHOD_FIELD_CONFIG.get();
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = beanProperties.get(i);
            Desensitize desensitizeField = writer.getAnnotation(Desensitize.class);
            Desensitize methodConfig = null;
            if (methodFieldConfig != null) {
                methodConfig = methodFieldConfig.get(writer.getName());
            }
            if ((desensitizeField != null && desensitizeField.enabled()) || methodConfig != null) {
                beanProperties.set(i, new DesensitizePropertyWriter(writer, desensitizeField, methodConfig));
            }
        }
        return beanProperties;
    }

    private static class DesensitizePropertyWriter extends BeanPropertyWriter {

        private final BeanPropertyWriter writer;
        private final Desensitize desensitizeField;
        private final Desensitize methodConfig;

        public DesensitizePropertyWriter(BeanPropertyWriter writer, Desensitize desensitizeField,
                                         Desensitize methodConfig) {
            super(writer);
            this.writer = writer;
            this.desensitizeField = desensitizeField;
            this.methodConfig = methodConfig;
        }

        @Override
        public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
            Object value = writer.get(bean);
            if (value != null && value instanceof String) {
                String strValue = (String) value;
                DesensitizeType type;
                String maskChar;

                if (methodConfig != null) {
                    type = methodConfig.type();
                    maskChar = methodConfig.maskChar();
                } else {
                    type = desensitizeField.type();
                    maskChar = desensitizeField.maskChar();
                }

                String desensitizedValue = desensitize(strValue, type, maskChar);
                gen.writeStringField(writer.getName(), desensitizedValue);
            } else {
                writer.serializeAsField(bean, gen, prov);
            }
        }

        private String desensitize(String value, DesensitizeType type, String maskChar) {
            if (maskChar == null || maskChar.isEmpty()) {
                maskChar = "*";
            }

            switch (type) {
                case MOBILE_PHONE:
                    return value.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1" + maskChar.repeat(4) + "$2");
                case EMAIL:
                    int atIndex = value.indexOf("@");
                    if (atIndex <= 1) {
                        return value;
                    }
                    String localPart = value.substring(0, atIndex);
                    String domainPart = value.substring(atIndex);
                    String maskedLocal = localPart.charAt(0) + maskChar.repeat(localPart.length() - 1);
                    return maskedLocal + domainPart;
                case ID_CARD:
                    return value.replaceAll("(\\d{3})\\d{10}(\\w{4})", "$1" + maskChar.repeat(10) + "$2");
                case CUSTOM:
                default:
                    return maskChar.repeat(value.length());
            }
        }
    }
}
