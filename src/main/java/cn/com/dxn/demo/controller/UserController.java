package cn.com.dxn.demo.controller;

import cn.com.dxn.demo.common.exception.ValidationException;
import cn.com.dxn.demo.common.system.ResponseEntity;
import cn.com.dxn.demo.model.entity.User;
import cn.com.dxn.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author richard
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping()
    public ResponseEntity getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity addUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("ok");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity editUser(@PathVariable Integer id, @RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().length() <= 0) {
            throw new ValidationException("用户名不能为空！");
        }
        return ResponseEntity.ok(userService.updateUser(id, user));
    }
}
