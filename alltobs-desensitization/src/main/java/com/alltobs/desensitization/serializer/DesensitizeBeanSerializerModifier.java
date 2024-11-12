package com.alltobs.desensitization.serializer;

import com.alltobs.desensitization.annotation.JsonDesensitize;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.Iterator;
import java.util.List;

/**
 * 类 BeanSerializerModifier
 *
 * @author ChenQi
 * &#064;date 2024/11/12
 */
public class DesensitizeBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {

        Iterator<BeanPropertyWriter> it = beanProperties.iterator();

        while (it.hasNext()) {
            BeanPropertyWriter writer = it.next();

            AnnotatedMember member = writer.getMember();

            JsonDesensitize desensitize = member.getAnnotation(JsonDesensitize.class);

            if (desensitize != null && desensitize.exclude()) {
                // 移除该字段，使其不被序列化
                it.remove();
            }
        }

        return beanProperties;
    }
}
