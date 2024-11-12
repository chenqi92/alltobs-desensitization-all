package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 对于标准11位手机号码，脱敏中间4位。
 * 对于大于11位的号码，脱敏中间部分，保持前3位和后4位可见。
 * 对于小于或等于4位的号码，全部替换为掩码字符。
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public class MobilePhoneDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        int length = value.length();

        if (length == 11) {
            // 标准11位手机号码，脱敏中间4位
            return value.substring(0, 3) + maskChar.repeat(4) + value.substring(7);
        } else if (length > 11) {
            // 长度超过11位，保持前3位和后4位，脱敏中间部分
            return value.substring(0, 3) + maskChar.repeat(length - 7) + value.substring(length - 4);
        } else if (length > 4) {
            // 长度不够11位但大于4位，脱敏中间部分
            int middleStart = length / 2 - 2;
            int middleEnd = middleStart + 4;
            return value.substring(0, middleStart) + maskChar.repeat(4) + value.substring(middleEnd);
        } else {
            // 小于等于4位，全部替换为掩码字符
            return maskChar.repeat(length);
        }
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "+";

        return "^\\d{3}" + maskPattern + "\\d{4}$|^" + // 标准11位或更长的脱敏格式
                "(\\d{" + maskPattern.length() + "})$"; // 匹配小于11位的格式
    }
}
