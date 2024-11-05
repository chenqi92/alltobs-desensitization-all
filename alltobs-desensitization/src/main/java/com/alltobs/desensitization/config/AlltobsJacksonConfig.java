package com.alltobs.desensitization.config;

import com.alltobs.desensitization.serializer.DesensitizeSerializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 配置类，注册脱敏模块，应用脱敏处理器。
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Configuration
public class AlltobsJacksonConfig {

    /**
     * 配置脱敏模块。
     *
     * @return SimpleModule 脱敏模块
     */
    @Bean
    public SimpleModule desensitizationModule() {
        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new DesensitizeSerializerModifier());
        return module;
    }
}
