package com.alltobs.desensitization.config;

import com.alltobs.desensitization.module.DesensitizeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;

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
