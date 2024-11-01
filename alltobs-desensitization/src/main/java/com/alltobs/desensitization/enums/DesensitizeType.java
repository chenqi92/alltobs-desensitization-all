package com.alltobs.desensitization.enums;

/**
 * 枚举 DesensitizeType 脱敏字段类型枚举
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
public enum DesensitizeType {

    DEFAULT,        // 使用父注解的类型
    CUSTOM,         // 自定义脱敏
    MOBILE_PHONE,   // 手机号
    EMAIL,          // 邮箱
    ID_CARD,        // 身份证号
    // TODO 适配其他类型
}
