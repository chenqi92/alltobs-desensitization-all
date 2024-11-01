package com.alltobs.desensitization;

import com.alltobs.desensitization.aspect.DesensitizeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 类 DesensitizationAutoConfiguration
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Configuration
@EnableAspectJAutoProxy
public class DesensitizationAutoConfiguration {

    @Bean
    public DesensitizeAspect desensitizeAspect() {
        return new DesensitizeAspect();
    }
}
