package com.demo.demo.sys.exception;

import lombok.NoArgsConstructor;

/**
 * 请求参数错误异常
 *
 * @author RCNJTECH
 * @date 2020/4/15 12:00
 */
@NoArgsConstructor
public class RequestParamErrorException extends RuntimeException {

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
