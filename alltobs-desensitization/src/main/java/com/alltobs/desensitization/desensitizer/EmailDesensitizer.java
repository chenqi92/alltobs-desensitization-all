package com.alltobs.desensitization.desensitizer;

import com.alltobs.desensitization.serializer.Desensitizer;

import java.util.regex.Pattern;

/**
 * 邮箱脱敏器
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public class EmailDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && value.contains("@")) {
            String[] parts = value.split("@", 2);
            String username = parts[0];
            String domain = parts[1];

            if (username.length() <= 1) {
                return maskChar + "@" + domain;
            } else if (username.length() == 2) {
                return username.charAt(0) + maskChar + "@" + domain;
            } else {
                String maskedUsername = username.charAt(0)
                        + maskChar.repeat(username.length() - 2)
                        + username.charAt(username.length() - 1);
                return maskedUsername + "@" + domain;
            }
        }
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "+";
        return "^[^@]" + maskPattern + "[^@]*@[^@]+$";
    }
}
