package cn.com.dxn.demo.common.exception;

import cn.com.dxn.demo.common.system.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginFailedException extends RuntimeException {
    private List<String> messages = new ArrayList<>();

    public LoginFailedException(){
        super("Login Failed");
        messages.add("Login Failed");
}

    public LoginFailedException(String... messages) {
        super("Login Failed");
        this.messages = Arrays.asList(messages);
    }

    public ResponseEntity getResponseEntity() {
        return ResponseEntity.create(HttpStatus.BAD_REQUEST.value(), messages, null);
    }
}
