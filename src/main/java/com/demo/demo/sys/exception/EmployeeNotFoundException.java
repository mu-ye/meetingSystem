package com.demo.demo.sys.exception;

import lombok.NoArgsConstructor;

/**
 * 职工不存在异常
 *
 * @author RCNJTECH
 * @date 2020/4/15 12:00
 */
@NoArgsConstructor
public class EmployeeNotFoundException extends RuntimeException {

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
