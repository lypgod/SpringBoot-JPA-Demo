package cn.com.dxn.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author richard
 */
@RestController
@RequestMapping(value = "/")
public class IndexController {

    @RequestMapping()
    public String index() {
        return "index";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value="/auth/user")
    public String adminTest1() {
        return "ROLE_USER";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/auth/admin")
    public String adminTest2() {
        return "ROLE_ADMIN";
    }
}
