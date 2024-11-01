package com.alltobs.desensitization.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类 JacksonConfig
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Configuration
public class AlltobsJacksonConfig {

    @Bean
    public SimpleModule desensitizationModule() {
        return new DesensitizationModule();
    }
}
