package com.alltobs.desensitization.annotation;

import com.alltobs.desensitization.DesensitizationAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解 EnableAllbsDesensitization
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({DesensitizationAutoConfiguration.class})
public @interface EnableAllbsDesensitization {
}
