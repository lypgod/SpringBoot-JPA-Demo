package cn.com.dxn.demo.model.repository;

import cn.com.dxn.demo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author richard
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * find user by username
     * @param username String username
     * @return User
     */
    User findByUsername(String username);
}
