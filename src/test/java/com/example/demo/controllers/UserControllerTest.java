package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.example.demo.TestUtils.createUser;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception{
        when(encoder.encode("testpassword")).thenReturn("thisIsHashed");
        CreateUserRequest newUser = new CreateUserRequest();
        newUser.setUsername("test");
        newUser.setPassword("testpassword");
        newUser.setConfirmPassword("testpassword");

        final ResponseEntity<User> response = userController.createUser(newUser);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void find_by_user_name(){
        User newUser = createUser();
        when(userRepo.findByUsername("test")).thenReturn(newUser);
        ResponseEntity<User> response = userController.findByUserName("test");

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());


        assertNotNull(newUser);
        assertEquals(1, response.getBody().getId());
        assertEquals("test", response.getBody().getUsername());
        assertEquals(newUser,response.getBody());
    }

    @Test
    public void find_by_id(){
        User newUser = createUser();
        when(userRepo.findById(1L)).thenReturn(Optional.of(newUser));
        ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User foundUser = response.getBody();

        assertNotNull(foundUser);
        assertEquals(1, foundUser.getId());
        assertEquals("test", foundUser.getUsername());
    }
}
