package com.alltobs.desensitization.desensitizer;

import com.alltobs.desensitization.serializer.Desensitizer;

/**
 * 邮箱脱敏器
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public class EmailDesensitizer implements Desensitizer {
    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && value.contains("@")) {
            String[] parts = value.split("@");
            String username = parts[0];
            String domain = parts[1];
            if (username.length() > 2) {
                return username.charAt(0) + maskChar.repeat(username.length() - 2)
                        + username.charAt(username.length() - 1) + "@" + domain;
            }
        }
        return value;
    }
}
