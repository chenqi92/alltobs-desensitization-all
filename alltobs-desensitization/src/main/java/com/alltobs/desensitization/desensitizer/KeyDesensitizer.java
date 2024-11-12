package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 用于将密钥进行脱敏，仅保留首位字符，其余部分脱敏。
 *
 * @author ChenQi
 * &#064;date 2024/11/12
 */
public class KeyDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && value.length() > 1) {
            // 保留首位字符，其他全部用掩码字符替换
            return value.charAt(0) + maskChar.repeat(value.length() - 1);
        }
        // 如果密钥长度为1或为空，直接返回原始值
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "+";

        // 正则匹配1位字符开头，后跟多个掩码字符
        return "^.{1}" + maskPattern + "$";
    }
}
