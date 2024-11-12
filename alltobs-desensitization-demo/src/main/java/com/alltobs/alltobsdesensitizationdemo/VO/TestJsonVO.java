package com.alltobs.alltobsdesensitizationdemo.VO;

import com.alltobs.alltobsdesensitizationdemo.desensitizer.IDCardDesensitizer;
import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.annotation.JsonDesensitize;
import com.alltobs.desensitization.desensitizer.EmailDesensitizer;
import com.alltobs.desensitization.desensitizer.MobilePhoneDesensitizer;
import com.alltobs.desensitization.enums.DesensitizeType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 类 TestVO
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Data
public class TestJsonVO {

    @JsonDesensitize(exclude = true)
    private String username;

    @JsonDesensitize(type = MobilePhoneDesensitizer.class)
    private String phoneNumber;

    @JsonDesensitize(type = EmailDesensitizer.class, maskChar = "#")
    private String email;

    @JsonDesensitize(type = IDCardDesensitizer.class)
    private String idCard;
}
