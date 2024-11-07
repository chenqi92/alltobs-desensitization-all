package com.alltobs.desensitization.serializer;

import com.alltobs.desensitization.desensitizer.DefaultDesensitizer;
import com.alltobs.desensitization.desensitizer.EmailDesensitizer;
import com.alltobs.desensitization.desensitizer.MobilePhoneDesensitizer;
import com.alltobs.desensitization.desensitizer.PasswordDesensitizer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类 DesensitizerRegistry
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public class DesensitizerRegistry {
    private static final Map<String, Desensitizer> desensitizers = new ConcurrentHashMap<>();

    // 静态代码块，用于注册默认的脱敏器
    static {
        registerDesensitizer("MOBILE_PHONE", new MobilePhoneDesensitizer());
        registerDesensitizer("EMAIL", new EmailDesensitizer());
        registerDesensitizer("PASSWORD", new PasswordDesensitizer());
        registerDesensitizer("DEFAULT", new DefaultDesensitizer());
    }

    /**
     * 注册脱敏器。
     *
     * @param typeName     脱敏类型名称
     * @param desensitizer 脱敏器实例
     */
    public static void registerDesensitizer(String typeName, Desensitizer desensitizer) {
        if (typeName == null || desensitizer == null) {
            throw new IllegalArgumentException("Type name and desensitizer cannot be null");
        }
        desensitizers.put(typeName.toUpperCase(), desensitizer);
    }

    /**
     * 获取脱敏器。
     *
     * @param typeName 脱敏类型名称
     * @return 对应的脱敏器，如果未找到则返回 null
     */
    public static Desensitizer getDesensitizer(String typeName) {
        if (typeName == null) {
            return null;
        }
        return desensitizers.get(typeName.toUpperCase());
    }
}
