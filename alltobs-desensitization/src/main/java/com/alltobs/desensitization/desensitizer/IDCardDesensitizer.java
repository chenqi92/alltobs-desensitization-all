package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 用于将身份证号的中间部分进行脱敏，仅保留前3位和后2位。
 *
 * @author ChenQi
 * &#064;date 2024/11/12
 */
public class IDCardDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && (value.length() == 15 || value.length() == 18)) {
            // 脱敏中间部分，保留前3位和后2位
            int prefixLength = 3;
            int suffixLength = 2;
            int maskLength = value.length() - prefixLength - suffixLength;

            String prefix = value.substring(0, prefixLength);
            String suffix = value.substring(value.length() - suffixLength);
            String maskedSection = maskChar.repeat(maskLength);
            return prefix + maskedSection + suffix;
        }
        // 对于非标准长度的身份证号，不进行脱敏
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "+";

        // 正则匹配3位前缀 + 脱敏字符 + 2位后缀，适用于15或18位的脱敏身份证号
        return "^\\d{3}" + maskPattern + "\\d{2}$";
    }
}
