package com.alltobs.desensitization.config;

import com.alltobs.desensitization.serializer.DesensitizeBeanSerializerModifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 配置类，注册脱敏模块，应用脱敏处理器。
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Configuration
public class AlltobsJacksonConfig {

    private final ObjectMapper objectMapper;

    public AlltobsJacksonConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 配置脱敏模块。
     *
     * @return SimpleModule 脱敏模块
     */
    @PostConstruct
    public void addDesensitizationModule() {
        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new DesensitizeBeanSerializerModifier());
        objectMapper.registerModule(module);
    }
}
