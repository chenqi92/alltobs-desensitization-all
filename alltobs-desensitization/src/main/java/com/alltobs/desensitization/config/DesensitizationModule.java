package com.alltobs.desensitization.config;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * 类 DesensitizationModule
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
public class DesensitizationModule extends SimpleModule {

    public DesensitizationModule() {
        super("DesensitizationModule", Version.unknownVersion());
    }

    @Override
    public void setupModule(SetupContext context) {
        // 调用父类的方法
        super.setupModule(context);
        // 添加自定义的 BeanDeserializerModifier
        context.addBeanDeserializerModifier(new DesensitizationBeanDeserializerModifier());
    }
}
