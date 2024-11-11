package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 类 DefaultDesensitizer
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public class DefaultDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null) {
            return maskChar.repeat(value.length());
        }
        return null;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        return "^" + escapedMaskChar + "+$";
    }
}
