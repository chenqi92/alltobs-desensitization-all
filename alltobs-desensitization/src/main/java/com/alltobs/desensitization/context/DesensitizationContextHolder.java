package com.alltobs.desensitization.context;

import com.alltobs.desensitization.annotation.Desensitize;

/**
 * 类 DesensitizationContextHolder
 *
 * @author ChenQi
 * &#064;date 2024/11/7
 */
public class DesensitizationContextHolder {
    private static final ThreadLocal<Desensitize[]> desensitizationRules = new ThreadLocal<>();

    public static void setDesensitizationRules(Desensitize[] rules) {
        desensitizationRules.set(rules);
    }

    public static Desensitize[] getDesensitizationRules() {
        return desensitizationRules.get();
    }

    public static void clear() {
        desensitizationRules.remove();
    }
}
