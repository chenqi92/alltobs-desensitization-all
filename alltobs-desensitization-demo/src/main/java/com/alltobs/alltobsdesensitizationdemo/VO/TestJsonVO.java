package com.alltobs.alltobsdesensitizationdemo.VO;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.annotation.JsonDesensitize;
import com.alltobs.desensitization.enums.DesensitizeType;
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

    @JsonDesensitize(type = DesensitizeType.MOBILE_PHONE)
    private String phoneNumber;

    @JsonDesensitize(type = DesensitizeType.EMAIL, maskChar = "#")
    private String email;
}
