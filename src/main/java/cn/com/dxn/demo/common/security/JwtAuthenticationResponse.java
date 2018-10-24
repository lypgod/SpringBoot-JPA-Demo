package cn.com.dxn.demo.common.security;

import cn.com.dxn.demo.model.entity.User;
import lombok.Data;
import lombok.NonNull;

/**
 * @author richard
 */
@Data
public class JwtAuthenticationResponse {
    @NonNull
    private User user;
    @NonNull
    private String token;
}