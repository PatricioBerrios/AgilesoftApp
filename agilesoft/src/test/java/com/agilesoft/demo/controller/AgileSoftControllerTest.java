package com.agilesoft.demo.controller;

import com.agilesoft.demo.dto.UserDTO;
import com.agilesoft.demo.service.AgileSoftService;
import com.agilesoft.demo.service.impl.ServiceToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgileSoftControllerTest {

    @Mock
    AgileSoftService agileSoftService;

    @Mock
    ServiceToken serviceToken;

    @InjectMocks
    AgileSoftController agileSoftController;

    @Test
    public void registerUserTest(){
        when(agileSoftService.registerUser(any()))
                .thenReturn(ResponseEntity.ok().build());
        ResponseEntity<?> response = agileSoftController.registerUser(any(UserDTO.class));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void loginUserTest(){
        when(agileSoftService.loginUser(any()))
                .thenReturn(ResponseEntity.ok().build());
        ResponseEntity<?> response = agileSoftController.loginUser(any(UserDTO.class));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void getUserDetailsTestInvalidHeaderTest() {
        String authHeader = "InvalidHeader";

        ResponseEntity<?> response = agileSoftController.getUserDetails(authHeader);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid authorization header format", response.getBody());
    }

    @Test
    public void getUserDetailsTestTest() {

        String jwt = "valid.jwt.token";
        String authHeader = "Bearer " + jwt;

        when(agileSoftController.getUserDetails(authHeader))
                .thenReturn(ResponseEntity.ok().build());
        assertEquals(200, agileSoftController.getUserDetails(authHeader).getStatusCode().value());

    }

    @Test
    public void getTasksByUsernameTest(){

        when(serviceToken.validateSession("jwt"))
                        .thenReturn("2");
        when(agileSoftService.getTasksByUsername("testUsername"))
                .thenReturn(ResponseEntity.ok().build());
        ResponseEntity<?> response = agileSoftController.getTasksByUsername("testUsername",
                "jwt");
        assertEquals(200, response.getStatusCode().value());

    }

    @Test
    public void getTasksByUsernameInvalidHeaderTest(){

        when(serviceToken.validateSession("jwt"))
                .thenReturn("2");
        when(agileSoftService.getTasksByUsername("testUsername"))
                .thenReturn(ResponseEntity.internalServerError().build());
        ResponseEntity<?> response = agileSoftController.getTasksByUsername("testUsername",
                "jwt");
        assertEquals(500, response.getStatusCode().value());

    }

    @Test
    public void getTasksByUsernameUnauthorizedHeaderTest(){

        when(serviceToken.validateSession("jwt"))
                .thenReturn("2");
        when(agileSoftService.getTasksByUsername("testUsername"))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        ResponseEntity<?> response = agileSoftController.getTasksByUsername("testUsername",
                "jwt");
        assertEquals(401, response.getStatusCode().value());

    }

    @Test
    public void postTasksTest(){

        when(serviceToken.validateSession("jwt"))
                .thenReturn("2");
        when(agileSoftService.postTasks(any()))
                .thenReturn(ResponseEntity.ok().build());
        ResponseEntity<?> response = agileSoftController.postTasks(any(), "jwt");
        assertEquals(200, response.getStatusCode().value());

    }

    @Test
    public void postTasksInvalidHeaderTest(){

        when(serviceToken.validateSession("jwt"))
                .thenReturn("2");
        when(agileSoftService.postTasks(any()))
                .thenReturn(ResponseEntity.internalServerError().build());
        ResponseEntity<?> response = agileSoftController.postTasks(any(), "jwt");
        assertEquals(500, response.getStatusCode().value());

    }

    @Test
    public void postTasksUnauthorizedHeaderTest(){

        when(serviceToken.validateSession("jwt"))
                .thenReturn("2");
        when(agileSoftService.postTasks(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        ResponseEntity<?> response = agileSoftController.postTasks(any(), "jwt");
        assertEquals(401, response.getStatusCode().value());

    }

}
