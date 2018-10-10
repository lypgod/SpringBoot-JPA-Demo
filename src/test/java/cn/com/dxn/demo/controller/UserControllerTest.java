package cn.com.dxn.demo.controller;

import cn.com.dxn.demo.model.entity.User;
import cn.com.dxn.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Resource
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void getAllUsersTest() throws Exception {
        User user1 = new User();
        user1.setId(1);
        user1.setUserName("user1");
        user1.setPassword("123456");
        user1.setMemo("memo1");

        User user2 = new User();
        user2.setId(2);
        user2.setUserName("user2");
        user2.setPassword("123456");
        user2.setMemo("memo2");

        Mockito.when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"code\": 200, \"messages\": [\"success\"], \"data\": [{\"id\": 1,\"userName\": \"user1\",\"password\": \"123456\",\"memo\": \"memo1\"}, {\"id\": 2,\"userName\": \"user2\",\"password\": \"123456\",\"memo\": \"memo2\"}] }"))
                .andReturn();
    }


    @Test
    public void addUserTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .content("{ \"userName\": \"demoData\", \"password\": \"demoData\", \"memo\": \"demoData\" }")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string("{\"code\":400,\"messages\":[\"用户名长度不能大于5\"],\"data\":null}"))
                .andReturn();

        requestBuilder = MockMvcRequestBuilders.post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .content("{ \"userName\": \"demo\", \"password\": \"demoData\", \"memo\": \"demoData\" }")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }
}