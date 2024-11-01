package com.alltobs.desensitization.config;

import com.alltobs.desensitization.annotation.Desensitize;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

import java.util.Iterator;
import java.util.List;

/**
 * 类 DesensitizationBeanDeserializerModifier
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
public class DesensitizationBeanDeserializerModifier extends BeanDeserializerModifier {

    @Override
    public BeanDeserializerBuilder updateBuilder(DeserializationConfig config, BeanDescription beanDesc, BeanDeserializerBuilder builder) {
        // 获取所有属性的定义
        List<BeanPropertyDefinition> properties = beanDesc.findProperties();

        for (BeanPropertyDefinition propDef : properties) {
            String propName = propDef.getName();

            // 获取字段上的 @Desensitize 注解
            AnnotatedField annotatedField = propDef.getField();

            if (annotatedField != null && annotatedField.hasAnnotation(Desensitize.class)) {
                Desensitize desensitize = annotatedField.getAnnotation(Desensitize.class);

                // 获取对应的 SettableBeanProperty
                SettableBeanProperty property = builder.findProperty(propDef.getFullName());

                if (property != null) {
                    // 为该字段设置自定义的反序列化器
                    JsonDeserializer<?> deserializer = property.getValueDeserializer();
                    property = property.withValueDeserializer(new DesensitizationFieldDeserializer(deserializer, desensitize));
                    builder.addOrReplaceProperty(property, true);
                }
            }
        }

        return builder;
    }
}
