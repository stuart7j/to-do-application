package com.jsserverspac.todoapplication.service;

import com.jsserverspac.todoapplication.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;

    // Constructor
    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
