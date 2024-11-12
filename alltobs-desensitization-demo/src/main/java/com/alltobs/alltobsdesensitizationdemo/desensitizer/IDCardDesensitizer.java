package com.alltobs.alltobsdesensitizationdemo.desensitizer;

import com.alltobs.desensitization.desensitizer.BaseDesensitizer;

import java.util.regex.Pattern;

/**
 * 类 IDCardDesensitizer
 *
 * @author ChenQi
 * &#064;date 2024/11/11
 */
public class IDCardDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && (value.length() == 15 || value.length() == 18)) {
            // 脱敏中间部分
            int prefixLength = 6;
            int suffixLength = 4;
            int maskLength = value.length() - prefixLength - suffixLength;

            String prefix = value.substring(0, prefixLength);
            String suffix = value.substring(value.length() - suffixLength);
            String maskedSection = maskChar.repeat(maskLength);
            return prefix + maskedSection + suffix;
        }
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        // 身份证号可能是 15 位或 18 位，这里统一处理
        String maskPattern = escapedMaskChar + "+";
        return "^\\d{6}" + maskPattern + "\\d{4}$";
    }
}
