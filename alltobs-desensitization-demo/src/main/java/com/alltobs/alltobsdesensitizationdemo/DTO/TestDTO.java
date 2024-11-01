package com.alltobs.alltobsdesensitizationdemo.DTO;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.enums.DesensitizeType;
import lombok.Data;

/**
 * 类 TestDTO
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Data
public class TestDTO {

    @Desensitize(exclude = true)
    private String username;

    @Desensitize(type = DesensitizeType.MOBILE_PHONE)
    private String phoneNumber;

    @Desensitize(type = DesensitizeType.EMAIL, maskChar = "#")
    private String email;
}
