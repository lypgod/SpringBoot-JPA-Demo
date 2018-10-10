package cn.com.dxn.demo.common.system;

import cn.com.dxn.demo.common.exception.AuthenticationException;
import cn.com.dxn.demo.common.exception.LoginFailedException;
import cn.com.dxn.demo.common.exception.NotFoundException;
import cn.com.dxn.demo.common.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity generalException(Exception e) {
        e.printStackTrace();
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), Collections.singletonList(e.getMessage()), null);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity validationException(ValidationException e) {
        return e.getResponseEntity();
    }

    @ExceptionHandler(LoginFailedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity loginException(LoginFailedException e) {
        return e.getResponseEntity();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity authenticationException(AuthenticationException e) {
        return e.getResponseEntity();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity notFoundException(NotFoundException e) {
        return e.getResponseEntity();
    }
}
