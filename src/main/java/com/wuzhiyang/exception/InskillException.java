package com.wuzhiyang.exception;

/**
 * 秒杀相关业务异常
 *
 * @author wzy
 */
public class InskillException extends RuntimeException {
    public InskillException(String message) {
        super(message);
    }

    public InskillException(String message, Throwable cause) {
        super(message, cause);
    }
}
