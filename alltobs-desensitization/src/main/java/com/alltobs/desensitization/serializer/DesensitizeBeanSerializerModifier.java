package com.alltobs.desensitization.serializer;

import com.alltobs.desensitization.annotation.JsonDesensitize;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 类 DesensitizeBeanSerializerModifier
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public class DesensitizeBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {


        Map<String, JsonDesensitize> methodDesensitizeMap = new HashMap<>();

        Iterator<BeanPropertyWriter> iterator = beanProperties.iterator();
        while (iterator.hasNext()) {
            BeanPropertyWriter writer = iterator.next();
            String fieldName = writer.getName();

            // 获取字段级别的 @Desensitize 注解
            JsonDesensitize fieldDesensitize = writer.getAnnotation(JsonDesensitize.class);

            JsonDesensitize desensitize = null;

            // 决定使用哪个脱敏规则
            if (fieldDesensitize != null) {
                desensitize = fieldDesensitize;
            }

            if (desensitize != null) {
                if (desensitize.exclude()) {
                    // 从属性列表中移除该字段
                    iterator.remove();
                } else {
                    // 为字段分配自定义序列化器
                    writer.assignSerializer(new DesensitizeSerializer(desensitize));
                }
            }
        }

        return beanProperties;
    }

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config,
                                              BeanDescription beanDesc,
                                              JsonSerializer<?> serializer) {
        return serializer;
    }
}
