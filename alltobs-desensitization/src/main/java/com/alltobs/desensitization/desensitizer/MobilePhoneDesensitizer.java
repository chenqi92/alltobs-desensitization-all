package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 类 MobilePhoneDesensitizer
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public class MobilePhoneDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && value.length() == 11) {
            return value.substring(0, 3) + maskChar.repeat(4) + value.substring(7);
        }
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "{4}";
        return "^\\d{3}" + maskPattern + "\\d{4}$";
    }
}
