package com.jsserverspac.todoapplication.service;
import com.jsserverspac.todoapplication.dto.TaskRequest;
import com.jsserverspac.todoapplication.dto.TaskResponse;
import com.jsserverspac.todoapplication.model.Task;
import com.jsserverspac.todoapplication.model.User;
import com.jsserverspac.todoapplication.repository.TaskRepository;
import com.jsserverspac.todoapplication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    private static final String USER_NOT_FOUND = "User not found";
    private static final String TASK_NOT_FOUND = "Task not found or unauthorized";

    public TaskService(TaskRepository taskRepo, UserRepository userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    public List<TaskResponse> getTasks(Authentication auth) {
        User user = getUser(auth);
        List<Task> tasks = taskRepo.findByUser(user);
        return tasks.stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList());
    }

    public TaskResponse createTask(TaskRequest req, Authentication auth) {

        // Validate input
        if (req.getTitle() == null || req.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title is required");
        }
        if (req.getDueDate() == null) {
            throw new IllegalArgumentException("Due date is required");
        }

        User user = getUser(auth);

        List<Task> existingTasks = taskRepo.findByTitleAndUser(req.getTitle(), user);
        if (!existingTasks.isEmpty()) {
            throw new IllegalArgumentException("Task with this title already exists for this user");
        }

        Task task = new Task();
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setDueDate(req.getDueDate());
        task.setCompleted(req.isCompleted());
        task.setUser(user);

        Task savedTask = taskRepo.save(task);
        return new TaskResponse(savedTask);
    }

    private User getUser(Authentication auth) {
        try {
            //extract username from auth
            String username = auth.getName();
            log.info("Username: {}", username);
            //find user by username
            return userRepo.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User details not found"));
        } catch (RuntimeException e) {
            throw new RuntimeException("User details not found");
        }
    }

    public TaskResponse getTask(Long id, Authentication auth) {
        if (id == null) {
            throw new IllegalArgumentException("Task ID is required");
        }

        User user = getUser(auth);
        Task task = taskRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Task not found or not authorized"));

        return new TaskResponse(task);
    }

    public TaskResponse updateTask(Long id, TaskRequest req, Authentication auth) {
        // Validate input
        if (id == null) {
            throw new IllegalArgumentException("Task ID is required");
        }
        if (req.getTitle() == null || req.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title is required");
        }
        if (req.getDueDate() == null) {
            throw new IllegalArgumentException("Due date is required");
        }

        User user = getUser(auth);
        Task task = taskRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Task not found or not authorized"));

        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setDueDate(req.getDueDate());
        task.setCompleted(req.isCompleted());

        Task updatedTask = taskRepo.save(task);
        return new TaskResponse(updatedTask);
    }

    public void deleteTask(Long id, Authentication auth) {
        if (id == null) {
            throw new IllegalArgumentException("Task ID is required");
        }

        User user = getUser(auth);
        Task task = taskRepo.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Task not found or not authorized"));

        taskRepo.delete(task);
    }
}
