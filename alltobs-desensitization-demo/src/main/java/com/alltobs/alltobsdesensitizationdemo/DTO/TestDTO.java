package com.alltobs.alltobsdesensitizationdemo.DTO;

import com.alltobs.desensitization.annotation.JsonDesensitize;
import com.alltobs.desensitization.desensitizer.EmailDesensitizer;
import com.alltobs.desensitization.desensitizer.MobilePhoneDesensitizer;
import lombok.Data;

/**
 * 类 TestDTO
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Data
public class TestDTO {

    @JsonDesensitize(exclude = true, ignoreDesensitized = true)
    private String username;

    @JsonDesensitize(type = MobilePhoneDesensitizer.class, ignoreDesensitized = true)
    private String phoneNumber;

    @JsonDesensitize(type = EmailDesensitizer.class, maskChar = "#", ignoreDesensitized = true)
    private String email;
}
