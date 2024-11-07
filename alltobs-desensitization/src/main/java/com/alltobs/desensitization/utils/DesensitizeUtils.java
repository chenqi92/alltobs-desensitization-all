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

        // 使用枚举中的mask方法处理脱敏
        return type != null ? type.mask(value, maskChar) : DesensitizeType.maskDefault(value, maskChar);
    }
}
