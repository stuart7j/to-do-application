package com.jsserverspac.todoapplication.repository;

import com.jsserverspac.todoapplication.model.Task;
import com.jsserverspac.todoapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByTitleAndUser(String title, User user);
}
