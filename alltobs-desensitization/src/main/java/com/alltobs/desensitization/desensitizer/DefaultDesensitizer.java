package com.alltobs.desensitization.desensitizer;

import com.alltobs.desensitization.serializer.Desensitizer;

/**
 * 类 DefaultDesensitizer
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public class DefaultDesensitizer implements Desensitizer {
    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null) {
            return maskChar.repeat(value.length());
        }
        return null;
    }
}
