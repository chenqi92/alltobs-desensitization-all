package com.alltobs.desensitization.enums;

import java.util.regex.Pattern;

/**
 * 脱敏类型枚举，用于定义不同的脱敏类型。
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
public enum DesensitizeType {

    /**
     * 默认脱敏规则
     */
    DEFAULT("default", "默认脱敏规则", ".*"),

    /**
     * 手机号脱敏
     */
    MOBILE_PHONE("mobile_phone", "手机号脱敏", "^1[3-9]\\d{9}$"),

    /**
     * 身份证脱敏
     */
    ID_CARD("id_card", "身份证脱敏", "^\\d{15}|\\d{18}$"),

    /**
     * 邮箱脱敏
     */
    EMAIL("email", "邮箱脱敏", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private final String code;
    private final String description;
    private final String regex;

    DesensitizeType(String code, String description, String regex) {
        this.code = code;
        this.description = description;
        this.regex = regex;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public Pattern getPattern() {
        return Pattern.compile(this.regex);
    }
}
