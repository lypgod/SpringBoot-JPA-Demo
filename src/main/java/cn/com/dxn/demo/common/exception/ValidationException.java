package cn.com.dxn.demo.common.exception;

import cn.com.dxn.demo.common.system.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidationException extends RuntimeException {
    private List<String> messages = new ArrayList<>();

    public ValidationException() {
        super("Validation Failed");
        this.messages.add("Validation Failed");
    }

    public ValidationException(String... messages) {
        super("Validation Failed");
        this.messages = Arrays.asList(messages);
    }

    public ValidationException(BindingResult bindingResult) {
        super("Validation Failed");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            this.messages.add(fieldError.getDefaultMessage());
        }
    }

    public ResponseEntity getResponseEntity() {
        return ResponseEntity.create(HttpStatus.BAD_REQUEST.value(), messages, null);
    }
}
