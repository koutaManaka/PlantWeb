package com.plant.entity;

import lombok.Data;


@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String token;

    public UserDTO(String username, Integer id, String token) {
        this.username = username;
        this.id = id;
        this.token = token;
    }

    public UserDTO() {

    }
}
