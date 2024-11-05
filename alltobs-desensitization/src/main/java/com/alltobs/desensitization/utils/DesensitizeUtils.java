package com.alltobs.desensitization.utils;

import com.alltobs.desensitization.enums.DesensitizeType;

import java.util.regex.Pattern;

/**
 * 类 DesensitizeUtils 脱敏工具
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
public class DesensitizeUtils {

    public static String desensitize(Object value, DesensitizeType type, String maskChar) {
        if (value == null) {
            return null;
        }
        String strValue = value.toString();
        switch (type) {
            case MOBILE_PHONE:
                return maskMobilePhone(strValue, maskChar);
            case EMAIL:
                return maskEmail(strValue, maskChar);
            case ID_CARD:
                return maskIdCard(strValue, maskChar);
            case CUSTOM:
            default:
                // 自定义脱敏逻辑
                return maskCustom(strValue, maskChar);
        }
    }

    // 脱敏手机号
    public static String maskMobilePhone(String phone, String maskChar) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + maskChar.repeat(4) + phone.substring(7);
    }

    // 脱敏邮箱
    public static String maskEmail(String email, String maskChar) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String localPart = parts[0];
        if (localPart.length() <= 1) {
            return maskChar + "@" + parts[1];
        }
        return localPart.charAt(0) + maskChar.repeat(localPart.length() - 1) + "@" + parts[1];
    }

    // 脱敏身份证号
    public static String maskIdCard(String idCard, String maskChar) {
        if (idCard == null || idCard.length() != 18) {
            return idCard;
        }
        return idCard.substring(0, 3) + maskChar.repeat(10) + idCard.substring(13);
    }

    // 自定义脱敏
    public static String maskCustom(String value, String maskChar) {
        if (value == null) {
            return null;
        }
        return maskChar.repeat(value.length());
    }

    public static String autoDesensitize(Object value) {
        if (value == null) {
            return null;
        }
        String strValue = value.toString();
        if (RegexUtils.isMobilePhone(strValue)) {
            return maskMobilePhone(strValue, "*");
        } else if (RegexUtils.isEmail(strValue)) {
            return maskEmail(strValue, "*");
        } else if (RegexUtils.isIdCard(strValue)) {
            return maskIdCard(strValue, "*");
        } else {
            // 对于其他情况，可以选择不脱敏，或者进行全局脱敏
            return strValue;
        }
    }

    public static boolean isDesensitizedMobilePhone(String value, String maskChar) {
        if (value == null || value.length() != 11) {
            return false;
        }
        if (maskChar == null || maskChar.isEmpty()) {
            maskChar = "*"; // Default mask character
        }
        String escapedMaskChar = Pattern.quote(maskChar);
        String regex = "^\\d{3}" + escapedMaskChar + "{4}\\d{4}$";
        return value.matches(regex);
    }

    public static boolean isDesensitizedEmail(String value, String maskChar) {
        if (value == null || !value.contains("@")) {
            return false;
        }
        String[] parts = value.split("@");
        String localPart = parts[0];
        String regex = ".*" + maskChar + "+.*";
        return localPart.matches(regex);
    }

    public static boolean isDesensitizedIdCard(String value, String maskChar) {
        if (value == null || value.length() != 18) {
            return false;
        }
        String regex = "^\\d{3}" + maskChar + "{10}[0-9Xx]{4}$";
        return value.matches(regex);
    }
}
