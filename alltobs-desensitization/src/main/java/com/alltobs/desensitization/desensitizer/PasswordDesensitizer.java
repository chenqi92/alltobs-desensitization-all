package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 类 PasswordDesensitizer
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public class PasswordDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && !value.isEmpty()) {
            return maskChar.repeat(value.length());
        }
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        return "^" + escapedMaskChar + "+$";
    }
}
