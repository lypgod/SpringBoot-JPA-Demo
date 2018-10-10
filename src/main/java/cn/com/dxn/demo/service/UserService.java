package cn.com.dxn.demo.service;

import cn.com.dxn.demo.common.exception.ValidationException;
import cn.com.dxn.demo.model.entity.User;
import cn.com.dxn.demo.model.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        if (userRepository.findByUserName(user.getUserName()) != null) {
            throw new ValidationException("用户名已经被注册！");
        }
        return userRepository.save(user);
    }

}
