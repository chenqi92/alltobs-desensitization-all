package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 类 NameDesensitizer
 *
 * @author ChenQi
 * &#064;date 2024/11/12
 */
public class NameDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && !value.isEmpty()) {
            if (value.length() == 2) {
                // 长度为2时，只保留第一个字符
                return value.charAt(0) + maskChar;
            } else if (value.length() > 2) {
                // 长度大于2时，保留首位和末位字符
                return value.charAt(0) + maskChar.repeat(value.length() - 2) + value.charAt(value.length() - 1);
            }
        }
        // 如果为空或仅有1个字符，直接返回原始值
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "+";

        // 正则匹配姓名脱敏格式：1个首字符，后跟多个掩码字符，最后1个尾字符
        return "^.{1}" + maskPattern + ".?$";
    }
}
