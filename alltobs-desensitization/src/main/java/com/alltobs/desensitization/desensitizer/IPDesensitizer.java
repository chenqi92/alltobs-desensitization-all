package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 用于将IP地址（包括IPv4和IPv6）进行脱敏。
 * 对于IPv4，保留前两段，对于IPv6，保留前两组，其余部分用掩码字符替代。
 *
 * @author ChenQi
 * &#064;date 2024/11/12
 */
public class IPDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        // 判断是否为 IPv4 地址
        if (value.contains(".")) {
            String[] segments = value.split("\\.");
            if (segments.length == 4) {
                // 保留前两段，脱敏后两段
                return segments[0] + "." + segments[1] + "."
                        + maskChar.repeat(segments[2].length()) + "."
                        + maskChar.repeat(segments[3].length());
            }
        }
        // 判断是否为 IPv6 地址
        else if (value.contains(":")) {
            String[] segments = value.split(":");
            if (segments.length >= 3) {
                // 保留前两组，脱敏后续组
                StringBuilder maskedIP = new StringBuilder();
                maskedIP.append(segments[0]).append(":").append(segments[1]);
                for (int i = 2; i < segments.length; i++) {
                    maskedIP.append(":").append(maskChar.repeat(segments[i].length()));
                }
                return maskedIP.toString();
            }
        }

        // 如果既不是有效的IPv4也不是IPv6，则返回原始值
        return value;
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "+";

        // 正则匹配：
        // 1. IPv4地址：前两段为数字，后两段为掩码字符
        // 2. IPv6地址：前两组为16进制字符，后续组为掩码字符
        String ipv4Pattern = "^\\d{1,3}\\.\\d{1,3}\\." + maskPattern + "\\." + maskPattern + "$";
        String ipv6Pattern = "^[\\da-fA-F]{1,4}:[\\da-fA-F]{1,4}(:(" + maskPattern + ")){1,6}$";

        return ipv4Pattern + "|" + ipv6Pattern;
    }
}
