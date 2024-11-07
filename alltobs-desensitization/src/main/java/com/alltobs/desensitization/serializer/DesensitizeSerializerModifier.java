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

    /**
     * 重写 modifySerializer 方法以应用脱敏逻辑。
     * 这是从 BeanSerializerModifier 中继承的方法，需要覆盖。
     *
     * @param config     序列化配置
     * @param beanDesc   Bean 描述
     * @param serializer 当前序列化器
     * @return 修改后的序列化器
     */
    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        // 获取类中的字段脱敏配置
        Map<String, Desensitize> fieldConfigMap = getFieldConfig(beanDesc.getBeanClass());

        if (fieldConfigMap.isEmpty()) {
            return serializer; // 如果没有脱敏配置，直接返回原始序列化器
        }

        // 创建新的序列化器，包装原始的序列化器，并添加脱敏逻辑
        return new GenericJsonSerializer<>(serializer, fieldConfigMap);
    }

    /**
     * 获取类中字段的脱敏配置
     *
     * @param clazz 要获取脱敏配置的类
     * @return 字段配置的映射
     */
    private static Map<String, Desensitize> getFieldConfig(Class<?> clazz) {
        Map<String, Desensitize> fieldConfigMap = new HashMap<>();

        // 通过反射获取类的字段和注解
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Desensitize desensitize = field.getAnnotation(Desensitize.class);
            if (desensitize != null) {
                fieldConfigMap.put(field.getName(), desensitize);
            }
        }

        return fieldConfigMap;
    }

    /**
     * 通用的 JsonSerializer，处理脱敏逻辑
     *
     * @param <T> 泛型类型
     */
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
                // 遍历字段配置，进行脱敏处理
                for (Map.Entry<String, Desensitize> entry : fieldConfigMap.entrySet()) {
                    String fieldName = entry.getKey();
                    Desensitize desensitize = entry.getValue();

                    try {
                        Field field = value.getClass().getDeclaredField(fieldName);
                        field.setAccessible(true);

                        // 如果需要排除字段，直接设置为 null
                        if (desensitize.exclude()) {
                            field.set(value, null);
                        } else {
                            // 获取字段的值
                            Object fieldValue = field.get(value);
                            if (fieldValue != null) {
                                // 根据脱敏类型应用脱敏
                                String maskChar = desensitize.maskChar();
                                DesensitizeType type = desensitize.type();

                                // 执行脱敏处理
                                String desensitizedValue = DesensitizeUtils.applyDesensitize(type, fieldValue.toString(), maskChar);

                                // 设置脱敏后的值回字段
                                field.set(value, desensitizedValue);
                            }
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        // 如果找不到字段或访问错误，直接跳过
                    }
                }
            }

            // 使用原始的序列化器将对象序列化
            originalSerializer.serialize(value, gen, serializers);
        }

        @Override
        public Class<T> handledType() {
            return (Class<T>) originalSerializer.handledType();
        }

    }
}
