package com.plant.entity;

import lombok.Data;


@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String token;
    private String deviceId;
    private String apiKey;
    public UserDTO(String username, Integer id, String token,String deviceId,String apiKey) {
        this.username = username;
        this.id = id;
        this.token = token;
        this.apiKey = apiKey;
        this.deviceId = deviceId;
    }

    public UserDTO() {

    }
}
