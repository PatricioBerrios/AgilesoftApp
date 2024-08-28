package com.agilesoft.demo.service;

import com.agilesoft.demo.dto.TaskDTO;
import com.agilesoft.demo.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface AgileSoftService {

    ResponseEntity<?> registerUser(UserDTO userDTO);
    ResponseEntity<?> loginUser(UserDTO userDTO);
    ResponseEntity<?> getUserDetails(String jwt);
    ResponseEntity<?> getTasksByUsername(String username);
    ResponseEntity<?> postTasks(TaskDTO task);
}
