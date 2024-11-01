package com.alltobs.alltobsdesensitizationdemo.controller;

import com.alltobs.alltobsdesensitizationdemo.VO.TestVO;
import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.annotation.DesensitizeField;
import com.alltobs.desensitization.enums.DesensitizeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类 TestController
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@RestController
public class TestController {

    @Desensitize(
            type = DesensitizeType.MOBILE_PHONE,
            maskChar = "^",
            fields = {
                    @Desensitize.Field(name = "username", exclude = true),
                    @Desensitize.Field(name = "phoneNumber"),
                    @Desensitize.Field(name = "email", type = DesensitizeType.EMAIL, exclude = false)
            }
    )
    @GetMapping("/test")
    public TestVO getUser() {
        TestVO test = new TestVO();
        test.setUsername("JohnDoe");
        test.setPhoneNumber("13812345678");
        test.setEmail("john.doe@example.com");
        return test;
    }
}
