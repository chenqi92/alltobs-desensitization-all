package com.alltobs.alltobsdesensitizationdemo.VO;

import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.enums.DesensitizeType;
import lombok.Data;

/**
 * 类 TestVO
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Data
public class TestVO {

    //    @Desensitize(exclude = true)
    private String username;

    //    @Desensitize(type = DesensitizeType.MOBILE_PHONE)
    private String phoneNumber;

    //    @Desensitize(type = DesensitizeType.EMAIL, maskChar = "#")
    private String email;
}
