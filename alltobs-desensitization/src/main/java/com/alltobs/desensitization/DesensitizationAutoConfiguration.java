package com.alltobs.desensitization;

import com.alltobs.desensitization.aspect.DesensitizeAspect;
import com.alltobs.desensitization.config.AlltobsJacksonConfig;
import com.alltobs.desensitization.handler.DesensitizationRequestBodyAdvice;
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
@Import({AlltobsJacksonConfig.class, DesensitizationRequestBodyAdvice.class, DesensitizeAspect.class})
public class DesensitizationAutoConfiguration {

}
