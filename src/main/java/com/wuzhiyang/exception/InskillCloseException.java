package com.wuzhiyang.exception;

/**
 * 秒杀关闭异常
 * Created by zhangyijun on 15/10/16.
 */
public class InskillCloseException extends InskillException {

    public InskillCloseException(String message) {
        super(message);
    }

    public InskillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
