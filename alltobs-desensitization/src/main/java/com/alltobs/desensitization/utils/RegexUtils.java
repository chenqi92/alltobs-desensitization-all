package com.alltobs.desensitization.utils;

import java.util.regex.Pattern;

/**
 * 类 RegexUtils 正则，用于不指定脱敏字段类型时自动匹配
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
public class RegexUtils {

    private static final Pattern MOBILE_PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^\\d{15}|\\d{18}$");

    public static boolean isMobilePhone(String str) {
        return MOBILE_PHONE_PATTERN.matcher(str).matches();
    }

    public static boolean isEmail(String str) {
        return EMAIL_PATTERN.matcher(str).matches();
    }

    public static boolean isIdCard(String str) {
        return ID_CARD_PATTERN.matcher(str).matches();
    }
}
