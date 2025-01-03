package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    // 账号唯一性错误
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        // Duplicate entry '刘勇' for key 'employee.idx_username'
        // 判断是否为唯一性错误
        if (ex.getMessage().contains("Duplicate entry")) {
            // 提取姓名
            String username = ex.getMessage().split("'")[1];
            // 账户已存在
            return Result.error(username + MessageConstant.ALREADY_EXIST);
        } else {
            // 未知错误
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }

    }
}
