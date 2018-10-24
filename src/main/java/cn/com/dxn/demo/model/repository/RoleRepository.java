package cn.com.dxn.demo.model.repository;

import cn.com.dxn.demo.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author richard
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    /**
     * find role by role name
     * @param rolename role name
     * @return Role
     */
    Role findByName(String rolename);
}
