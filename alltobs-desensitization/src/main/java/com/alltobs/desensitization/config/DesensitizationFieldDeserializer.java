package com.alltobs.desensitization.config;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.enums.DesensitizeType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 类 DesensitizationFieldDeserializer
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
public class DesensitizationFieldDeserializer extends JsonDeserializer<Object> {

    private final JsonDeserializer<?> defaultDeserializer;
    private final Desensitize desensitize;

    public DesensitizationFieldDeserializer(JsonDeserializer<?> defaultDeserializer, Desensitize desensitize) {
        this.defaultDeserializer = defaultDeserializer;
        this.desensitize = desensitize;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // 先使用默认的反序列化器获取字段值
        Object value = defaultDeserializer.deserialize(p, ctxt);

        if (value instanceof String strValue) {
            String maskChar = desensitize.maskChar();
            DesensitizeType type = desensitize.type();

            if (isDesensitized(strValue, type, maskChar)) {
                // 返回 null，表示忽略该字段
                return null;
            }
        }

        return value;
    }

    private boolean isDesensitized(String value, DesensitizeType type, String maskChar) {
        if (maskChar == null || maskChar.isEmpty()) {
            maskChar = "*"; // 默认脱敏字符
        }

        // 转义脱敏字符
        String escapedMaskChar = Pattern.quote(maskChar);

        switch (type != null ? type : DesensitizeType.CUSTOM) {
            case MOBILE_PHONE:
                String mobileRegex = "^\\d{3}" + escapedMaskChar + "{4}\\d{4}$";
                return value.matches(mobileRegex);
            case EMAIL:
                int atIndex = value.indexOf("@");
                if (atIndex <= 0) {
                    return false;
                }
                String localPart = value.substring(0, atIndex);
                String emailRegex = ".*" + escapedMaskChar + "+.*";
                return localPart.matches(emailRegex);
            case ID_CARD:
                String idCardRegex = "^\\d{3}" + escapedMaskChar + "{10}[0-9Xx]{4}$";
                return value.matches(idCardRegex);
            case CUSTOM:
            default:
                String customRegex = ".*" + escapedMaskChar + "+.*";
                return value.matches(customRegex);
        }
    }
}
