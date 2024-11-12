package com.alltobs.desensitization;

import com.alltobs.desensitization.aspect.DesensitizeAspect;
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
@Import({DesensitizeAspect.class})
public class DesensitizationAutoConfiguration {

}
