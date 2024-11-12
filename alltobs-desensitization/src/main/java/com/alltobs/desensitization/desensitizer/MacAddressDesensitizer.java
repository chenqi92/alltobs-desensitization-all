package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 用于将MAC地址进行脱敏，仅保留前两组字符，其余部分用掩码字符替代。
 *
 * @author ChenQi
 * &#064;date 2024/11/12
 */
public class MacAddressDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        // 判断是否为有效的MAC地址
        String[] segments = value.split("[:-]");
        if (segments.length == 6) {
            // 保留前两组，脱敏后四组
            return segments[0] + ":" + segments[1] + ":"
                    + maskChar.repeat(2) + ":"
                    + maskChar.repeat(2) + ":"
                    + maskChar.repeat(2) + ":"
                    + maskChar.repeat(2);
        }

        // 如果不是有效的MAC地址格式，则返回原始值
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "{2}";

        // 正则匹配MAC地址脱敏格式：前两组字符，后四组为掩码字符
        return "^[\\da-fA-F]{2}:[\\da-fA-F]{2}(:(" + maskPattern + ")){4}$";
    }
}
