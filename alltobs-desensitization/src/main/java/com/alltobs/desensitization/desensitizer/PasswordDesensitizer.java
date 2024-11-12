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
            // 将整个密码用掩码字符替换，保持长度一致
            return maskChar.repeat(value.length());
        }
        // 返回原始值（适用于空或null的情况）
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        // 正则匹配完全脱敏后的密码（仅包含掩码字符）
        return "^" + escapedMaskChar + "+$";
    }
}
