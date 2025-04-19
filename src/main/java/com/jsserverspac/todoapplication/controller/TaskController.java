package com.jsserverspac.todoapplication.controller;
import com.jsserverspac.todoapplication.dto.TaskRequest;
import com.jsserverspac.todoapplication.dto.TaskResponse;
import com.jsserverspac.todoapplication.model.Task;
import com.jsserverspac.todoapplication.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskResponse>> getTasks(Authentication auth) {
        try {
            List<TaskResponse> responses = taskService.getTasks(auth);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(new TaskResponse("Failed to retrieve tasks: " + e.getMessage())));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id, Authentication auth) {
        try {
            TaskResponse response = taskService.getTask(id, auth);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new TaskResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TaskResponse("Failed to retrieve task: " + e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest req, Authentication auth) {
        try {
            TaskResponse response = taskService.createTask(req, auth);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new TaskResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TaskResponse("Failed to create task: " + e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @Valid @RequestBody TaskRequest req, Authentication auth) {
        try {
            TaskResponse response = taskService.updateTask(id, req, auth);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new TaskResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TaskResponse("Failed to update task: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TaskResponse> delete(@PathVariable Long id, Authentication auth) {
        try {
            taskService.deleteTask(id, auth);
            return ResponseEntity.ok(new TaskResponse("Task deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new TaskResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TaskResponse("Failed to delete task: " + e.getMessage()));
        }
    }
}
