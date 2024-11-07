package com.alltobs.desensitization.utils;

import com.alltobs.desensitization.enums.DesensitizeType;

import java.util.regex.Matcher;

/**
 * 类 DesensitizeUtils
 *
 * @author ChenQi
 * &#064;date 2024/11/6
 */
public class DesensitizeUtils {

    public static void validateMaskChar(String maskChar) {
        if ("$".equals(maskChar)) {
            throw new IllegalArgumentException("The mask character '$' is not allowed.");
        }
    }

    /**
     * 应用脱敏规则
     *
     * @param type     脱敏类型
     * @param value    需要脱敏的值
     * @param maskChar 脱敏字符
     * @return 脱敏后的值
     */
    public static String applyDesensitize(DesensitizeType type, String value, String maskChar) {
        if (maskChar == null || maskChar.isEmpty()) {
            maskChar = "*";  // 设置默认脱敏字符
        }

        return switch (type) {
            case MOBILE_PHONE -> maskMobilePhone(value, maskChar);
            case EMAIL -> maskEmail(value, maskChar);
            default -> maskDefault(value, maskChar);
        };
    }

    private static String maskMobilePhone(String value, String maskChar) {
        if (value != null && value.length() > 4) {
            return value.substring(0, 3) + maskChar.repeat(4) + value.substring(7);
        }
        return value;
    }

    private static String maskEmail(String value, String maskChar) {
        if (value != null && value.contains("@")) {
            String[] parts = value.split("@");
            return parts[0].charAt(0) + maskChar.repeat(parts[0].length() - 2) + parts[0].charAt(parts[0].length() - 1) + "@" + parts[1];
        }
        return value;
    }

    private static String maskDefault(String value, String maskChar) {
        if (value != null) {
            return maskChar.repeat(value.length());
        }
        return value;
    }
}
