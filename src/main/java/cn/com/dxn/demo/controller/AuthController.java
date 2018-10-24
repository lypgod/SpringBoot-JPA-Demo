package cn.com.dxn.demo.controller;

import cn.com.dxn.demo.common.exception.ValidationException;
import cn.com.dxn.demo.common.system.ResponseEntity;
import cn.com.dxn.demo.model.entity.User;
import cn.com.dxn.demo.service.AuthService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author richard
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Resource
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid User addedUser, BindingResult bindingResult) throws AuthenticationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return ResponseEntity.ok(authService.register(addedUser));
    }

    @GetMapping("/logout")
    public ResponseEntity logout() throws AuthenticationException {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("ok");
    }

//    @GetMapping("/auth/refresh")
//    public ResponseEntity refreshTocken(HttpServletRequest request) throws Exception {
//        String token = request.getHeader(tokenHeader);
//        String refreshedToken = authService.refresh(token);
//        if (refreshedToken == null) {
//            throw new Exception("Token refresh failed!");
//        } else {
//            return ResponseEntity.ok(new JwtAuthenticationResponse(null, refreshedToken));
//        }
//    }
}