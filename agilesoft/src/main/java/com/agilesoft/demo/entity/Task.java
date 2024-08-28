package com.agilesoft.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String state;
    private String description;
    private String username;
    private boolean isActive;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;

}
