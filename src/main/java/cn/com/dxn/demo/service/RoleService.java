package cn.com.dxn.demo.service;

import cn.com.dxn.demo.model.entity.Role;
import cn.com.dxn.demo.model.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author richard
 */
@Service
public class RoleService {
    @Resource
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
