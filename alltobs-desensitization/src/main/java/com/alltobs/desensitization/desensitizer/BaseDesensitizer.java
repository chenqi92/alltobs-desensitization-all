package com.alltobs.desensitization.desensitizer;

import com.alltobs.desensitization.serializer.Desensitizer;

import java.util.regex.Pattern;

/**
 * 基础脱敏器，实现了 Desensitizer 接口，提供默认的 containsSensitiveData 实现。
 *
 * @author ChenQi
 * &#064;date 2024/11/11
 */
public abstract class BaseDesensitizer implements Desensitizer {

    @Override
    public abstract String desensitize(String value, String maskChar);

    @Override
    public abstract String getDesensitizedRegex(String maskChar);
}
