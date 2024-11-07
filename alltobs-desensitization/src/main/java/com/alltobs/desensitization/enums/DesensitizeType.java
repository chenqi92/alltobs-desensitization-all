package com.alltobs.desensitization.enums;

import java.util.regex.Pattern;

/**
 * 脱敏类型枚举，用于定义不同的脱敏类型。
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
public enum DesensitizeType {
    MOBILE_PHONE {
        @Override
        public String mask(String value, String maskChar) {
            if (value != null && value.length() > 4) {
                return value.substring(0, 3) + maskChar.repeat(4) + value.substring(7);
            }
            return value;
        }
    },
    EMAIL {
        @Override
        public String mask(String value, String maskChar) {
            if (value != null && value.contains("@")) {
                String[] parts = value.split("@");
                return parts[0].charAt(0) + maskChar.repeat(parts[0].length() - 2) + parts[0].charAt(parts[0].length() - 1) + "@" + parts[1];
            }
            return value;
        }
    },
    PASSWORD {
        @Override
        public String mask(String value, String maskChar) {
            if (value != null) {
                return maskChar.repeat(value.length());
            }
            return value;
        }
    },
    // 默认脱敏规则
    DEFAULT {
        @Override
        public String mask(String value, String maskChar) {
            if (value != null) {
                return maskChar.repeat(value.length()); // 用默认字符替换整个值
            }
            return value;
        }
    };

    // 抽象方法，由每个枚举常量实现具体的脱敏逻辑
    public abstract String mask(String value, String maskChar);

    // 默认规则方法，用于处理不明确指定的类型
    public static String maskDefault(String value, String maskChar) {
        return DEFAULT.mask(value, maskChar); // 默认规则处理
    }
}
