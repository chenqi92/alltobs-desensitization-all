package com.alltobs.desensitization.desensitizer;

import com.alltobs.desensitization.serializer.Desensitizer;

/**
 * 类 MobilePhoneDesensitizer
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public class MobilePhoneDesensitizer implements Desensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && value.length() > 7) {
            return value.substring(0, 3) + maskChar.repeat(4) + value.substring(7);
        }
        return value;
    }
}
