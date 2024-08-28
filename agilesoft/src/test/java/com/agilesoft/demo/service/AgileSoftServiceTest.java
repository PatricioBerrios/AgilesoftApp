package com.agilesoft.demo.service;

import com.agilesoft.demo.dto.TaskDTO;
import com.agilesoft.demo.dto.UserDTO;
import com.agilesoft.demo.entity.Task;
import com.agilesoft.demo.entity.User;
import com.agilesoft.demo.repository.TaskRepository;
import com.agilesoft.demo.repository.UserRepository;
import com.agilesoft.demo.service.impl.AgileSoftServiceImpl;
import com.agilesoft.demo.service.impl.ServiceToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgileSoftServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    ServiceToken serviceToken;

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    AgileSoftServiceImpl agileSoftService;

    @Test
    public void registerUserTest(){
        when(serviceToken.encryptPassword("pass"))
                .thenReturn("newpass");

        when(userRepository.save(any(User.class)))
                .thenReturn(getUser());

        ResponseEntity<?> response = agileSoftService.registerUser(getUserDTO());

        assertEquals(200, response.getStatusCode().value());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    public void registerUserExceptionTest(){
        when(serviceToken.encryptPassword("pass"))
                .thenThrow(new RuntimeException());

        ResponseEntity<?> response = agileSoftService.registerUser(getUserDTO());
        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    public void getUserDetailsTest(){

        when(serviceToken.extractUsername("jwt"))
                .thenReturn(anyString());
        when(userRepository.findByUsername("prueba"))
                .thenReturn(new User());

        ResponseEntity<?> response = agileSoftService.getUserDetails("jwt");
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void getUserDetailsNotFoundTest(){

        when(serviceToken.extractUsername("jwt"))
                .thenReturn(anyString());
        when(userRepository.findByUsername("prueba"))
                .thenReturn(null);

        ResponseEntity<?> response = agileSoftService.getUserDetails("jwt");
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void getTasksByUsernameTest(){
        when(taskRepository.findByUsernameAndIsActive("user", true))
                .thenReturn(getListTasks());

        ResponseEntity<?> response = agileSoftService.getTasksByUsername("user");
        assertEquals(200, response.getStatusCode().value());

    }

    @Test
    public void getTasksByUsernameEmptyListTest(){
        when(taskRepository.findByUsernameAndIsActive("user", true))
                .thenReturn(new ArrayList<>());

        ResponseEntity<?> response = agileSoftService.getTasksByUsername("user");
        assertEquals(404, response.getStatusCode().value());

    }

    @Test
    public void postTasksTest(){
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(getTask()));
        ResponseEntity<?> response = agileSoftService.postTasks(getTaskDTO());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void postTasksEmptyTest(){
        when(taskRepository.findById(1L))
                .thenReturn(Optional.empty());
        ResponseEntity<?> response = agileSoftService.postTasks(getTaskDTO());
        assertEquals(200, response.getStatusCode().value());
    }

    private TaskDTO getTaskDTO() {
        TaskDTO task = new TaskDTO();
        task.setId(1L);
        task.setDescription("desc");
        task.setName("name");
        task.setState("state");
        task.setActive(true);
        task.setCreationDate(LocalDateTime.of(2024, 10, 10, 0, 0));
        task.setLastUpdateDate(LocalDateTime.of(2024, 10, 10, 0, 0));
        return task;
    }

    private Task getTask() {
        Task task = new Task();
        task.setId(1L);
        task.setDescription("desc");
        task.setName("name");
        task.setState("state");
        task.setActive(true);
        task.setCreationDate(LocalDateTime.of(2024, 10, 10, 0, 0));
        task.setLastUpdateDate(LocalDateTime.of(2024, 10, 10, 0, 0));
        return task;

    }

    private List<Task> getListTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setId(1L);
        tasks.add(task);
        return tasks;
    }

    private User getUser() {
        User user = new User();
        user.setPassword("pass");
        user.setId(1778L);
        user.setName("Prueba");
        user.setUsername("prueba");
        return user;
    }

    private UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Prueba");
        userDTO.setUsername("prueba");
        userDTO.setPassword("pass");
        return userDTO;
    }

}
