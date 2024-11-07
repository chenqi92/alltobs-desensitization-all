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
public class TestNoAnnoVO {

    private String username;

    private String phoneNumber;

    private String email;
}
