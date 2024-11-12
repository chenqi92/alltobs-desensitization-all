package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 用于脱敏电子邮件地址的用户名部分，保留首尾字符
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
                // 处理长度为1的用户名，全部脱敏
                return maskChar + "@" + domain;
            } else if (username.length() == 2) {
                // 处理长度为2的用户名，仅保留第一个字符
                return username.charAt(0) + maskChar + "@" + domain;
            } else {
                // 处理长度大于2的用户名，保留首尾字符
                String maskedUsername = username.charAt(0) + maskChar.repeat(username.length() - 2) + username.charAt(username.length() - 1);
                return maskedUsername + "@" + domain;
            }
        }
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "+";

        // 正则表达式：
        // ^ 表示开始，[^@] 表示非 @ 的第一个字符，
        // `maskPattern` 匹配掩码字符的重复，[^@]+ 表示域名部分
        return "^[^@]" + maskPattern + "[^@]?@[^@]+$";
    }
}
