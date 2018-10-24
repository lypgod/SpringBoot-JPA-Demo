package cn.com.dxn.demo.service;

import cn.com.dxn.demo.common.security.SysUser;
import cn.com.dxn.demo.model.entity.User;
import cn.com.dxn.demo.model.repository.RoleRepository;
import cn.com.dxn.demo.model.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author richard
 */
@Service
public class AuthService implements UserDetailsService {
    @Resource
    private UserService userService;
    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;

    public User register(User userToAdd) {
        userToAdd.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));
        return userService.saveUser(userToAdd);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        return new SysUser(user);
    }
}