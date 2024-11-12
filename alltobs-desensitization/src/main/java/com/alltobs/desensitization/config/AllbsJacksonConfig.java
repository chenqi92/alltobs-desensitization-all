package com.alltobs.desensitization.config;

import com.alltobs.desensitization.module.DesensitizeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 类 AllbsJacksonConfig
 *
 * @author ChenQi
 * &#064;date 2024/11/12
 */
public class AllbsJacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 注册自定义模块
        mapper.registerModule(new DesensitizeModule());
        return mapper;
    }
}
