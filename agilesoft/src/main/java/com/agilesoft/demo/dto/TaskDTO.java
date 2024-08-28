package com.agilesoft.demo.dto;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDTO {

    private Long id;
    private String name;
    private String state;
    private String description;
    private String username;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
    private boolean isActive;

}
