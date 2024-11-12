package com.alltobs.desensitization.desensitizer;

import java.util.regex.Pattern;

/**
 * 用于将中国大陆车牌号进行脱敏。
 * 普通车牌：保留前两位和最后一位字符，中间部分脱敏。
 * 新能源车牌：保留前三位和最后一位字符，中间部分脱敏。
 *
 * @author ChenQi
 * &#064;date 2024/11/12
 */
public class LicensePlateDesensitizer extends BaseDesensitizer {

    @Override
    public String desensitize(String value, String maskChar) {
        if (value != null && !value.isEmpty()) {
            if (isNewEnergyPlate(value)) {
                // 新能源车牌（8 位）：保留前 3 位和最后 1 位
                return value.substring(0, 3)
                        + maskChar.repeat(4)
                        + value.substring(7);
            } else if (value.length() == 7) {
                // 普通车牌（7 位）：保留前 2 位和最后 1 位
                return value.substring(0, 2)
                        + maskChar.repeat(4)
                        + value.substring(6);
            }
        }
        // 如果不符合车牌格式或为空，返回原始值
        return value;
    }

    /**
     * 判断是否为新能源车牌
     *
     * @param value 车牌号
     * @return 是否为新能源车牌
     */
    private boolean isNewEnergyPlate(String value) {
        return value.length() == 8 && (value.startsWith("D") || value.startsWith("F"));
    }

    @Override
    public String getDesensitizedRegex(String maskChar) {
        String escapedMaskChar = Pattern.quote(maskChar);
        String maskPattern = escapedMaskChar + "{4}";

        // 正则匹配：
        // 普通车牌（7位）：前2位字符 + 中间掩码字符 + 最后一位字符
        // 新能源车牌（8位）：前3位字符 + 中间掩码字符 + 最后一位字符
        String normalPlatePattern = "^[\\u4e00-\\u9fa5A-Z]{2}" + maskPattern + "[A-Z0-9]$";
        String newEnergyPlatePattern = "^[DF][A-Z0-9]{2}" + maskPattern + "[A-Z0-9]$";

        return normalPlatePattern + "|" + newEnergyPlatePattern;
    }
}
