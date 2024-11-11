package com.alltobs.desensitization.serializer;

/**
 * 脱敏器接口，用于定义脱敏逻辑。
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public interface Desensitizer {
    /**
     * 执行脱敏操作。
     *
     * @param value    原始值
     * @param maskChar 脱敏字符
     * @return 脱敏后的值
     */
    String desensitize(String value, String maskChar);

    /**
     * 生成用于匹配脱敏后数据的正则表达式。
     *
     * @param maskChar 脱敏字符
     * @return 正则表达式字符串
     */
    String getDesensitizedRegex(String maskChar);
}
