package com.alltobs.desensitization.module;

import com.alltobs.desensitization.serializer.DesensitizeBeanSerializerModifier;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * 类 DesensitizeModule
 *
 * @author ChenQi
 * &#064;date 2024/11/12
 */
public class DesensitizeModule extends SimpleModule {

    public DesensitizeModule() {
        super("DesensitizeModule", Version.unknownVersion());
        this.setSerializerModifier(new DesensitizeBeanSerializerModifier());
    }
}
