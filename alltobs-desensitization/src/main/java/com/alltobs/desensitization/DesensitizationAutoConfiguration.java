package com.alltobs.desensitization;

import com.alltobs.desensitization.aspect.DesensitizeAspect;
import com.alltobs.desensitization.config.AllbsJacksonConfig;
import com.alltobs.desensitization.module.DesensitizeModule;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * 类 DesensitizationAutoConfiguration
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Configuration
@EnableAspectJAutoProxy
@Import({DesensitizeAspect.class, AllbsJacksonConfig.class})
public class DesensitizationAutoConfiguration {

}
