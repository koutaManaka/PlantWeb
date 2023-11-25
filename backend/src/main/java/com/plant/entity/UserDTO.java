package com.plant.entity;

import lombok.Data;


@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String token;
    private String deviceId;
    private String apiKey;
    private String role;
    public UserDTO(String username, Integer id, String token,String deviceId,String apiKey,String role) {
        this.username = username;
        this.id = id;
        this.token = token;
        this.apiKey = apiKey;
        this.deviceId = deviceId;
        this.role = role;
    }

    public UserDTO() {

    }
}
