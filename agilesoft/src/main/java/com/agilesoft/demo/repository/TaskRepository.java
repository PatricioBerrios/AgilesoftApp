package com.agilesoft.demo.repository;

import com.agilesoft.demo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUsernameAndIsActive(String username, boolean isActive);

}
