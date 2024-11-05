package com.alltobs.alltobsdesensitizationdemo.DTO;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.annotation.ValidateDesensitize;
import com.alltobs.desensitization.enums.DesensitizeType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * 类 TestDTO
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Data
public class TestDTO {

    private String username;

//    @ValidateDesensitize(type = DesensitizeType.MOBILE_PHONE, enabled = true)
    private String phoneNumber;

//    @ValidateDesensitize(type = DesensitizeType.MOBILE_PHONE, enabled = true, maskChar = "#")
    private String email;
}
