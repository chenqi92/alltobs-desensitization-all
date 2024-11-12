package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 用于将地址的详细信息进行脱敏。对于长度大于3的地址，仅保留前3位；
 * 如果长度不足3位，则整个地址进行脱敏。
 *
 * @author ChenQi
 * &#064;date 2024/11/12
 */
public class AddressDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && !value.isEmpty()) {
            if (value.length() <= 3) {
                // 如果地址长度小于或等于3，则全部脱敏
                return maskChar.repeat(value.length());
            } else {
                // 长度大于3，保留前3位，后续部分全部脱敏
                int prefixLength = 3;
                int maskLength = value.length() - prefixLength;

                String prefix = value.substring(0, prefixLength);
                String maskedSection = maskChar.repeat(maskLength);
                return prefix + maskedSection;
            }
        }
        // 如果地址为空或为null，则返回原始值
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "+";

        // 正则匹配：
        // 1. 如果原地址长度大于3位，匹配前3个字符+多个掩码字符；
        // 2. 如果原地址长度小于或等于3位，匹配全部掩码字符
        return "^(.{3}" + maskPattern + ")|(" + maskPattern + ")$";
    }
}
