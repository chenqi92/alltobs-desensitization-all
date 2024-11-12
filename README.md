
## 功能说明
因为最近有脱敏的需求，所以就趁着这个机会自己实现了一个。肯定还有不少开源库有这种功能，但是考虑到不一定满足自己的需要，所以就自己造了一个轮子自己用。主要功能包括
- 接口返回内容的脱敏，包含json序列化方式和aop的两种实现。
- json序列化方式只能用于接口数据返回，在程序内部和数据库并不脱敏。
- aop的方式，在程序内部和接口数据返回时可以脱敏，但是数据库不脱敏，且速度是慢于序列化方式的。
- 根据自己的需求来实现脱敏规则，比如有些人的手机号脱敏是中间四位，有些人又只保留初始和末尾一位。
- 自定义脱敏字符，默认为`*`。
- 请求体数据自动过滤，比如你传向前端的数据是包含脱敏字符的，正常的做法是前端判断时候包含脱敏字符，如果包含则该字段不往后端传输。我这边实现的是，如果请求体传过来的数据内容跟改字段的脱敏规则一致则**不接收**该字段内容，不，准确的说是在接收该字段内容之前转为null。

## 使用示例
总共有四种使用场景：

### 1.作用与方法上的aop注解
```java
@Desensitizes({@Desensitize(field = "username", exclude = true), @Desensitize(field = "phoneNumber", type = MobilePhoneDesensitizer.class), @Desensitize(field = "email", type = EmailDesensitizer.class, maskChar = "#")})  
@GetMapping("/testMethodAnno")  
public TestNoAnnoVO testMethodAnno() {  
    TestNoAnnoVO test = new TestNoAnnoVO();  
    test.setUsername("JohnDoe");  
    test.setPhoneNumber("13812345678");  
    test.setEmail("john.doe@example.com");  
    return test;  
}

@Data  
public class TestNoAnnoVO {  
  
    private String username;  
  
    private String phoneNumber;  
  
    private String email;  
}
```
输出内容为:
![作用与方法的注解](https://nas.allbs.cn:9006/cloudpic/2024/11/4fbcc8e29713e50e1de559074a08dc7d.png)

### 2.作用于实体字段的aop注解
```java
@GetMapping("/testFieldAnno")  
public TestVO testFieldAnno() {  
    TestVO test = new TestVO();  
    test.setUsername("JohnDoe");  
    test.setPhoneNumber("13812345678");  
    test.setEmail("john.doe@example.com");  
    test.setIdCard("123456199001011234");  
    return test;  
}

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
```
输出内容为：
![作用于实体字段的aop注解](https://nas.allbs.cn:9006/cloudpic/2024/11/0336cdb3267b0826010c035365ddd90d.png)

### 3.序列化注解
```java
@GetMapping("/testJsonAnno")  
public TestJsonVO testJsonAnno() {  
    TestJsonVO test = new TestJsonVO();  
    test.setUsername("JohnDoe");  
    test.setPhoneNumber("13812345678");  
    test.setEmail("j@example.com");  
    test.setIdCard("123456199001011234");  
    return test;  
}

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
```
这个有些区别，如果设置了exclude为true，则直接将返回的该字段隐去。输出内容为:
![序列化注解](https://nas.allbs.cn:9006/cloudpic/2024/11/d6876fdc270a595a80e59244cf4fe3a9.png)

### 4.最后就是基于`@JsonDesensitize`额外加了一个属性`ignoreDesensitized`，用于判断是否在接收数据时排除脱敏内容。
```java
@PostMapping("testPut")  
public void testPut(@RequestBody TestDTO testDTO) {  
    log.info("接收到的数据内容为{}", testDTO.toString());  
}

@Data  
public class TestDTO {  
  
    @JsonDesensitize(exclude = true, ignoreDesensitized = true)  
    private String username;  
  
    @JsonDesensitize(type = MobilePhoneDesensitizer.class, ignoreDesensitized = true)  
    private String phoneNumber;  
  
    @JsonDesensitize(type = EmailDesensitizer.class, maskChar = "#", ignoreDesensitized = true)  
    private String email;  
  
    @JsonDesensitize(type = IDCardDesensitizer.class, ignoreDesensitized = true)  
    private String idCard;  
}
```
使用效果为：
![在接收数据时排除脱敏内容](https://nas.allbs.cn:9006/cloudpic/2024/11/9ccce02a25273dfa7c93d919a4dea70b.png)
这个是严格按照输出时的脱敏匹配的，比如输出时手机号脱敏内容为`138****1234`,那么`136****4321`则不会接收，正常没有`*`的手机号或者不符合该规则的比如`13****51234`这种也会被正常接收。当然这个规则是可以在项目中自定义的。

## 使用说明
### 项目地址
https://github.com/chenqi92/alltobs-desensitization-all
里面包含待引入的jar和demo，因为刚写完，简单测试了一下不排除有其他bug，有需要可以自己fork一下自己改。后续我会在项目中实际使用，如果发现有bug会不断更新，当然也欢迎提issuses。

### 引入jar
```xml
<dependency>  
    <groupId>com.alltobs</groupId>  
    <artifactId>alltobs-desensitization</artifactId>  
    <version>1.0.0</version>  
</dependency>
```
工具已经上传到了maven的中央仓库，国内的用户可能存在下载困难等问题，需要科学一下。使用国内的阿里等平台代理，以我以往经验同步过去起码要半个月把。
![](https://nas.allbs.cn:9006/cloudpic/2024/11/8ab6aec184d394e859abde3918d1d4b1.png)


### 启用功能
启动类添加注解`@EnableAllbsDesensitization`

### 根据自己使用场景的需要、以及上述示例来选择性的添加注解
- `@Desensitize`
- `@Desensitizes`
- `@JsonDesensitize`

### 自定义在项目中实现一个自己需要的脱敏方式
如果你不想用默认的脱敏方式，实际上我也没写几个，后续应该会接着加。
那么就在你的项目工程中自定义集成`BaseDesensitizer`类然后自己实现`desensitize`和`getDesensitizedRegex`方法即可。
这两个方法一个是用于脱敏规则，一个是用于接收数据时判断是否符合规则。
具体实现以身份证号为例:
```java
package com.alltobs.alltobsdesensitizationdemo.desensitizer;  
  
import com.alltobs.desensitization.desensitizer.BaseDesensitizer;  
  
import java.util.regex.Pattern;  
  
/**  
 * 类 IDCardDesensitizer  
 * * @author ChenQi  
 * &#064;date 2024/11/11  
 */public class IDCardDesensitizer extends BaseDesensitizer {  
  
    @Override  
    public String desensitize(String value, String maskChar) {  
        if (value != null && (value.length() == 15 || value.length() == 18)) {  
            // 脱敏中间部分  
            int prefixLength = 6;  
            int suffixLength = 4;  
            int maskLength = value.length() - prefixLength - suffixLength;  
  
            String prefix = value.substring(0, prefixLength);  
            String suffix = value.substring(value.length() - suffixLength);  
            String maskedSection = maskChar.repeat(maskLength);  
            return prefix + maskedSection + suffix;  
        }  
        return value;  
    }  
  
    @Override  
    public String getDesensitizedRegex(String maskChar) {  
        String escapedMaskChar = Pattern.quote(maskChar);  
        // 身份证号可能是 15 位或 18 位，这里统一处理  
        String maskPattern = escapedMaskChar + "+";  
        return "^\\d{6}" + maskPattern + "\\d{4}$";  
    }  
}
```
![demo中的示例实现](https://nas.allbs.cn:9006/cloudpic/2024/11/45691837c4fb73d06fcd2c4e7533e1ef.png)
