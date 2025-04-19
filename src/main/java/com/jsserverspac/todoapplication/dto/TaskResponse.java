package com.jsserverspac.todoapplication.dto;

import com.jsserverspac.todoapplication.model.Task;
import com.jsserverspac.todoapplication.service.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
    private UserResponse user;
    private String error; // For error messages

    public TaskResponse() {}

    public TaskResponse(String error) {
        this.error = error;
    }

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.dueDate = task.getDueDate();
        this.completed = task.isCompleted();
        this.user = new UserResponse(task.getUser());
    }

}
