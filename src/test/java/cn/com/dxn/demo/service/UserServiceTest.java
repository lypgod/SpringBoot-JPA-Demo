package cn.com.dxn.demo.service;

import cn.com.dxn.demo.common.exception.ValidationException;
import cn.com.dxn.demo.model.entity.User;
import cn.com.dxn.demo.model.repository.UserRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getAllUsersTest() {
        User user1 = new User();
        user1.setUserName("user1");
        user1.setPassword("123456");
        user1.setMemo("memo1");

        User user2 = new User();
        user2.setUserName("user2");
        user2.setPassword("123456");
        user2.setMemo("memo2");

        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        assertEquals(userService.getAllUsers().size(), 2);
    }

    @Test
    public void saveUserTest() {
        User user1 = new User();
        user1.setId(1);
        user1.setUserName("user1");
        user1.setPassword("123456");
        user1.setMemo("memo1");
        Mockito.when(userRepository.save(user1)).thenReturn(user1);

        Mockito.when(userRepository.findByUserName(user1.getUserName())).thenReturn(null);
        assertEquals(userService.saveUser(user1), user1);

        Mockito.when(userRepository.findByUserName(user1.getUserName())).thenReturn(user1);
        thrown.expect(ValidationException.class);
        userService.saveUser(user1);
    }
}