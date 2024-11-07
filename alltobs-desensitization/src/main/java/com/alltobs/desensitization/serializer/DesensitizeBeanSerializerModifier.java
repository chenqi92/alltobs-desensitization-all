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

        Iterator<BeanPropertyWriter> iterator = beanProperties.iterator();
        while (iterator.hasNext()) {
            BeanPropertyWriter writer = iterator.next();

            // 获取字段级别的 @JsonDesensitize 注解
            JsonDesensitize desensitize = writer.getAnnotation(JsonDesensitize.class);

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
