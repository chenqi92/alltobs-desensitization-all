package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 用于将座机号的中间部分进行脱敏，保留区号和末尾两位数字。
 *
 * @author ChenQi
 * &#064;date 2024/11/12
 */
public class LandlineDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && value.length() > 6) {
            // 假设区号长度为3或4位，保留区号和末尾2位数字
            int prefixLength = value.startsWith("0") ? 4 : 3; // 如果以0开头，假设区号4位，否则3位
            int suffixLength = 2;
            int maskLength = value.length() - prefixLength - suffixLength;

            String prefix = value.substring(0, prefixLength);
            String suffix = value.substring(value.length() - suffixLength);
            String maskedSection = maskChar.repeat(maskLength);
            return prefix + maskedSection + suffix;
        }
        // 如果号码太短，不进行脱敏
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "+";

        // 正则匹配区号 + 脱敏字符 + 尾部2位数字
        return "^\\d{3,4}" + maskPattern + "\\d{2}$";
    }
}
