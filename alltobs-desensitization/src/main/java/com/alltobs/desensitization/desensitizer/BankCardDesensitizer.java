package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 用于将银行卡号进行脱敏。通常保留前4位和后4位数字，中间部分脱敏。
 * 对于长度不足8位的银行卡号，整个号码进行脱敏。
 *
 * @author ChenQi
 * &#064;date 2024/11/12
 */
public class BankCardDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && !value.isEmpty()) {
            if (value.length() >= 8) {
                // 长度大于等于8位，保留前4位和后4位
                int prefixLength = 4;
                int suffixLength = 4;
                int maskLength = value.length() - prefixLength - suffixLength;

                String prefix = value.substring(0, prefixLength);
                String suffix = value.substring(value.length() - suffixLength);
                String maskedSection = maskChar.repeat(maskLength);
                return prefix + maskedSection + suffix;
            } else {
                // 长度不足8位，全部脱敏
                return maskChar.repeat(value.length());
            }
        }
        // 如果为空或为null，直接返回原始值
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "+";

        // 正则匹配：
        // 1. 如果银行卡号长度大于等于8位：前4位 + 中间掩码字符 + 后4位
        // 2. 如果长度不足8位，匹配全部掩码字符
        return "^\\d{4}" + maskPattern + "\\d{4}$|" + "^" + maskPattern + "$";
    }
}
