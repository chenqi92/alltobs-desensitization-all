package com.alltobs.alltobsdesensitizationdemo.controller;

import com.alltobs.alltobsdesensitizationdemo.DTO.TestDTO;
import com.alltobs.alltobsdesensitizationdemo.VO.TestVO;
import com.alltobs.desensitization.annotation.Desensitize;
import com.alltobs.desensitization.enums.DesensitizeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类 TestController
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
@Slf4j
@RestController
public class TestController {

//    @Desensitize(
//            type = DesensitizeType.MOBILE_PHONE,
//            maskChar = "^",
//            fields = {
//                    @Desensitize.Field(name = "username", exclude = true),
//                    @Desensitize.Field(name = "phoneNumber"),
//                    @Desensitize.Field(name = "email", type = DesensitizeType.EMAIL, exclude = false)
//            }
//    )
    @GetMapping("/test")
    public TestVO getUser() {
        TestVO test = new TestVO();
        test.setUsername("JohnDoe");
        test.setPhoneNumber("13812345678");
        test.setEmail("john.doe@example.com");
        return test;
    }

    /**
     * 测试数据修改，添加注解的字段如果包含脱敏的内容，则忽略接收
     */
    @PostMapping("testPut")
    public void testPut(@RequestBody TestDTO testDTO) {
        log.info("接收到的数据内容为{}", testDTO.toString());
    }
}
