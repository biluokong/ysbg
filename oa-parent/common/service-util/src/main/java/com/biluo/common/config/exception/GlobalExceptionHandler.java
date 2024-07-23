package com.biluo.common.config.exception;

import com.biluo.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //全局异常处理，执行的方法
    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        e.printStackTrace();
        log.error("全局异常处理器：{}", e.getMessage());
        return Result.fail().message("执行全局异常处理...");
    }

    //特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    public Result error(ArithmeticException e) {
        e.printStackTrace();
        log.error("全局异常处理器：{}", e.getMessage());
        return Result.fail().message("执行特定异常处理...");
    }

    //自定义异常处理
    @ExceptionHandler(GlobalException.class)
    public Result error(GlobalException e) {
        //e.printStackTrace();
        log.error("全局异常处理器：{}", e.getMessage());
        return Result.fail().code(e.getCode()).message(e.getMsg());
    }

    /**
     * spring security异常
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result error(AccessDeniedException e) {
        return Result.fail().code(205).message("没有操作权限");
    }
}
