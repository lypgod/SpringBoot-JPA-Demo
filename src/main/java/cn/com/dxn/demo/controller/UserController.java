package cn.com.dxn.demo.controller;

import cn.com.dxn.demo.common.exception.ValidationException;
import cn.com.dxn.demo.common.system.ResponseEntity;
import cn.com.dxn.demo.model.entity.User;
import cn.com.dxn.demo.service.UserService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping()
    public ResponseEntity getAllUsers() {
        return ResponseEntity.create(userService.getAllUsers());
    }

    @PostMapping()
    public User addUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return userService.saveUser(user);
    }
}
