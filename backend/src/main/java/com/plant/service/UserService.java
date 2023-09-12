package com.plant.service;

import com.plant.entity.Result;
import com.plant.pojo.User;

public interface UserService {

    Result login(User loginUser);

    Result register(User registerUser);

    void sendMsg(String phone, String subject, String context);

    Result change(User user);

    User findByEmail(String email);
}
