package com.web.common;


import com.web.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public R<Employee> myExceptionHandler(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException){
        String message = sqlIntegrityConstraintViolationException.getMessage();


        if (message.contains("Duplicate entry")){
            String[] s = message.split(" ");
            log.info(s[2]);


            return R.error(s[2]+"已存在");
        }



        return R.error("系统繁忙,请稍后再试");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> myExceptionHandler(CustomException customException){
        String message = customException.getMessage();

        return R.error(message);
    }
}
