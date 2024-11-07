package com.alltobs.desensitization.serializer;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.enums.DesensitizeType;
import com.alltobs.desensitization.utils.DesensitizeUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义序列化修改器，用于根据注解修改字段的序列化行为。
 * 主要实现根据字段的脱敏注解，应用对应的脱敏处理。
 *
 * @author ChenQi
 * &#064;date 2024/11/5
 */
public class DesensitizeSerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        Map<String, Desensitize> fieldConfigMap = getFieldConfig(beanDesc.getBeanClass());

        if (fieldConfigMap.isEmpty()) {
            return serializer; // 如果没有脱敏配置，直接返回原始序列化器
        }

        return new GenericJsonSerializer<>(serializer, fieldConfigMap);
    }

    private static Map<String, Desensitize> getFieldConfig(Class<?> clazz) {
        Map<String, Desensitize> fieldConfigMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Desensitize desensitize = field.getAnnotation(Desensitize.class);
            if (desensitize != null) {
                fieldConfigMap.put(field.getName(), desensitize);
            }
        }
        return fieldConfigMap;
    }

    public static class GenericJsonSerializer<T> extends JsonSerializer<T> {

        private final JsonSerializer<T> originalSerializer;
        private final Map<String, Desensitize> fieldConfigMap;

        public GenericJsonSerializer(JsonSerializer<T> originalSerializer, Map<String, Desensitize> fieldConfigMap) {
            this.originalSerializer = originalSerializer;
            this.fieldConfigMap = fieldConfigMap;
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                for (Map.Entry<String, Desensitize> entry : fieldConfigMap.entrySet()) {
                    String fieldName = entry.getKey();
                    Desensitize desensitize = entry.getValue();

                    try {
                        Field field = value.getClass().getDeclaredField(fieldName);
                        field.setAccessible(true);

                        if (desensitize.exclude()) {
                            field.set(value, null);
                        } else {
                            Object fieldValue = field.get(value);
                            if (fieldValue != null) {
                                String maskChar = desensitize.maskChar();
                                DesensitizeType type = desensitize.type();

                                // 使用 DesensitizeUtils.applyDesensitize 来处理脱敏
                                String desensitizedValue = DesensitizeUtils.applyDesensitize(type, fieldValue.toString(), maskChar);

                                field.set(value, desensitizedValue);
                            }
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        // 如果找不到字段或访问错误，跳过
                    }
                }
            }

            originalSerializer.serialize(value, gen, serializers);
        }

        @Override
        public Class<T> handledType() {
            return (Class<T>) originalSerializer.handledType();
        }
    }
}
