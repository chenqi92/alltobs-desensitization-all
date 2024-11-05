package com.alltobs.desensitization.enums;

/**
 * 脱敏类型枚举，用于定义不同的脱敏类型。
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
public enum DesensitizeType {

    /**
     * 默认类型，使用父注解的脱敏类型。
     */
    DEFAULT,

    /**
     * 自定义脱敏。
     */
    CUSTOM,

    /**
     * 手机号脱敏。
     */
    MOBILE_PHONE,

    /**
     * 邮箱脱敏。
     */
    EMAIL,

    /**
     * 身份证号脱敏。
     */
    ID_CARD,
    // TODO: 适配其他类型的脱敏
}
