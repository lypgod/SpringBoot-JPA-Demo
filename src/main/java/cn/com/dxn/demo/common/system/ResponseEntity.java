package cn.com.dxn.demo.common.system;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class ResponseEntity {
    private Integer code;
    private List<String> messages;
    private Object data;

    public static ResponseEntity create(Object data) {
        return new ResponseEntity(200, Collections.singletonList("success"), data);
    }

    public static ResponseEntity create(Integer code, List<String> messages, Object data) {
        return new ResponseEntity(code, messages, data);
    }
}
