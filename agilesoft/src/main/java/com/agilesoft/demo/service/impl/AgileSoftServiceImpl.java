package com.agilesoft.demo.service.impl;

import com.agilesoft.demo.dto.TaskDTO;
import com.agilesoft.demo.dto.UserDTO;
import com.agilesoft.demo.entity.Task;
import com.agilesoft.demo.entity.User;
import com.agilesoft.demo.repository.TaskRepository;
import com.agilesoft.demo.repository.UserRepository;
import com.agilesoft.demo.service.AgileSoftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import io.jsonwebtoken.SignatureException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgileSoftServiceImpl implements AgileSoftService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServiceToken serviceToken;

    @Autowired
    TaskRepository taskRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<?> registerUser(UserDTO userDTO) {

        try{
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(serviceToken.encryptPassword(userDTO.getPassword()));
            user.setName(userDTO.getName());

            userRepository.save(user);

            return ResponseEntity.ok("User registered successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering user: " + e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> loginUser(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername());

        if (user != null && passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.ok("Bearer " + serviceToken.getJwtToken(user.getUsername()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    @Override
    public ResponseEntity<?> getUserDetails(String jwt) {
        try {
            String username = serviceToken.extractUsername(jwt);
            User user = userRepository.findByUsername(username);

            if (user != null) {

                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(user.getUsername());
                userDTO.setName(user.getName());

                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
        } catch (SignatureException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid JWT signature");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the token");
        }
    }

    @Override
    public ResponseEntity<?> getTasksByUsername(String username) {

        try {

            List<Task> tasks = taskRepository.findByUsernameAndIsActive(username, true);

            if (tasks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No tasks found for user: " + username);
            }

            List<TaskDTO> taskDTOs = tasks.stream()
                    .map(task -> {
                        TaskDTO taskDTO = new TaskDTO();
                        taskDTO.setId(task.getId());
                        taskDTO.setName(task.getName());
                        taskDTO.setState(task.getState());
                        taskDTO.setDescription(task.getDescription());
                        taskDTO.setUsername(task.getUsername());
                        taskDTO.setCreationDate(task.getCreationDate());
                        taskDTO.setLastUpdateDate(task.getLastUpdateDate());
                        return taskDTO;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(taskDTOs);

        }catch (DataAccessException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error occurred: " + e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> postTasks(TaskDTO task) {

        try {

            Task existingTask = taskRepository.findById(task.getId()).orElse(null);

            Task taskDB = new Task();
            taskDB.setName(task.getName());
            taskDB.setState(task.getState());
            taskDB.setDescription(task.getDescription());
            taskDB.setCreationDate(task.getCreationDate());
            taskDB.setLastUpdateDate(task.getLastUpdateDate());
            taskDB.setActive(task.isActive());
            taskDB.setUsername(task.getUsername());

            if (existingTask == null) {
                taskRepository.save(taskDB);
                return ResponseEntity.ok("Task added successfully");
            } else {
                taskRepository.save(taskDB);
                return ResponseEntity.ok("Task updated successfully");
            }
        }catch (DataAccessException e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Database error occurred: " + e.getMessage());
        }
    }


}
