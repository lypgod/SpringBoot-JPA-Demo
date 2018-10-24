package cn.com.dxn.demo.controller;

import cn.com.dxn.demo.common.system.ResponseEntity;
import cn.com.dxn.demo.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author richard
 */
@RestController
@RequestMapping("/roles")
public class RoleController {
    @Resource
    private RoleService roleService;

    @GetMapping()
    public ResponseEntity getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}
