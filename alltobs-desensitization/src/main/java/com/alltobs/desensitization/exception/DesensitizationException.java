package com.alltobs.desensitization.exception;

/**
 * 类 DesensitizationException 脱敏异常
 *
 * @author ChenQi
 * &#064;date 2024/11/1
 */
public class DesensitizationException extends RuntimeException {

    public DesensitizationException() {
        super();
    }

    public DesensitizationException(String message) {
        super(message);
    }

    public DesensitizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
