package com.alltobs.desensitization.utils;

import com.alltobs.desensitization.enums.DesensitizeType;

import static com.alltobs.desensitization.enums.DesensitizeType.*;

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
        return switch (type) {
            case MOBILE_PHONE -> maskMobilePhone(strValue, maskChar);
            case EMAIL -> maskEmail(strValue, maskChar);
            case ID_CARD -> maskIdCard(strValue, maskChar);
            case CUSTOM -> strValue.replaceAll("\\.", maskChar);
            default -> strValue;
        };
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
            return strValue.replaceAll("\\.", "*");
        }
    }

    private static String maskMobilePhone(String phone, String maskChar) {
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1" + maskChar.repeat(4) + "$2");
    }

    private static String maskEmail(String email, String maskChar) {
        int index = email.indexOf("@");
        if (index <= 1) {
            return email;
        }
        return email.charAt(0) + maskChar.repeat(index - 1) + email.substring(index);
    }

    private static String maskIdCard(String idCard, String maskChar) {
        return idCard.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1" + maskChar.repeat(10) + "$2");
    }
}
