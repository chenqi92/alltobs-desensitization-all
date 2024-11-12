package com.alltobs.alltobsdesensitizationdemo.VO;

import com.alltobs.alltobsdesensitizationdemo.desensitizer.IDCardDesensitizer;
import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.desensitizer.EmailDesensitizer;
import com.alltobs.desensitization.desensitizer.MobilePhoneDesensitizer;
import lombok.Data;

/**
 * 类 TestVO
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Data
public class TestVO {

    @Desensitize(exclude = true)
    private String username;

    @Desensitize(type = MobilePhoneDesensitizer.class)
    private String phoneNumber;

    @Desensitize(type = EmailDesensitizer.class, maskChar = "#")
    private String email;

    @Desensitize(type = IDCardDesensitizer.class, maskChar = "%")
    private String idCard;
}
