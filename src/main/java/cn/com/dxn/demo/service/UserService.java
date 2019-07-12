package cn.com.dxn.demo.service;

import cn.com.dxn.demo.common.exception.NotFoundException;
import cn.com.dxn.demo.common.exception.ValidationException;
import cn.com.dxn.demo.model.entity.User;
import cn.com.dxn.demo.model.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author richard
 */
@Service
@Log
public class UserService {
    @Resource
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new ValidationException("用户名已经被注册！");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setLastPasswordResetDate(new Date());
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Integer id, User modifiedUser) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new NotFoundException("用户不存在！"));

        if (!modifiedUser.getUsername().equals(userToUpdate.getUsername())) {
            if (userRepository.findByUsername(modifiedUser.getUsername()) != null) {
                throw new ValidationException("用户名已经被注册！");
            }
            userToUpdate.setUsername(modifiedUser.getUsername());
        }
        userToUpdate.setMemo(modifiedUser.getMemo());
        return userRepository.save(userToUpdate);
    }
}
