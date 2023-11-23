package com.plant.entity;

import lombok.Data;

@Data
public class UsersDTO {
    private Integer id;
    private String role;
    private String username;
    private String deviceId;
    private String apiKey;
    private String email;

    public UsersDTO(String username, Integer id, String role,String deviceId,String apiKey,String email) {
        this.username = username;
        this.id = id;
        this.email = email;
        this.role = role;
        this.apiKey = apiKey;
        this.deviceId = deviceId;
    }

    public UsersDTO() {

    }
}
