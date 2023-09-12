package com.plant.entity;

import lombok.Data;


@Data
public class UserDTO {
    private String username;
    private String password;
    private String token;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
