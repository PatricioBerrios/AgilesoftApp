package com.agilesoft.demo.controller;
import com.agilesoft.demo.dto.TaskDTO;
import com.agilesoft.demo.dto.UserDTO;
import com.agilesoft.demo.service.AgileSoftService;
import com.agilesoft.demo.service.impl.ServiceToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class AgileSoftController {

    @Autowired
    AgileSoftService agileSoftService;

    @Autowired
    ServiceToken serviceToken;

    //Servicio para inscribir un usuario
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        return agileSoftService.registerUser(userDTO);
    }

    //Servicio de login de usuario
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {
        return agileSoftService.loginUser(userDTO);
    }

    //Servicio de Obtener datos usuario a partir de sesión
    @GetMapping("/user/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authorizationHeader) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            return agileSoftService.getUserDetails(jwt);
        } else {
            return ResponseEntity.badRequest().body("Invalid authorization header format");
        }
    }

    //Servicio de Obtener listado de tareas del usuario y su estado
    @GetMapping("/tasks/{username}")
    public ResponseEntity<?> getTasksByUsername(@PathVariable String username,
                                                @RequestHeader("Authorization") String authorizationHeader) {
        try {
            serviceToken.validateSession(authorizationHeader);
            return agileSoftService.getTasksByUsername(username);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud.");
        }
    }

    /* Servicio de Agregar tarea asociada al usuario
       Marcar como resuelta una tarea de un usuario
       Eliminar una tarea de un usuario
       Con Spring JPA, es posible unificar estos tres servicios en un único servicio, esto
       debido a la versatilidad del método save del repositorio JPA, el cual inserta en la base de datos
       si el id no existe y en caso contrario, de existir el id, actualiza el registro. Como es una muy mala práctica
       eliminar registros de la base de datos, cree una propiedad adicional llamada "isActive", que al ser false
       no traerá el registro cuando se hagan operaciones de obtener tareas por usuario o similares.


     */

    @PostMapping("tasks")
    public ResponseEntity<?> postTasks(@RequestBody TaskDTO task,
                                       @RequestHeader("Authorization") String authorizationHeader){
        try {
            serviceToken.validateSession(authorizationHeader);
            return agileSoftService.postTasks(task);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud.");
        }
    }

}
