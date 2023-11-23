package com.plant.service;

import com.plant.entity.Result;
import com.plant.entity.UsersDTO;
import com.plant.pojo.User;

import java.util.List;

public interface UserService {

    void edit(User editUser);

    Result login(User loginUser);

    Result register(User registerUser);

    void sendMsg(String phone, String subject, String context);

    Result change(User user);

    User findById(String id);

    User findByEmail(String email);

    List<UsersDTO> selectPage(Integer pageNum, Integer pageSize,String username,String email,String apiKey);

    Integer getTotal(String username,String email,String apiKey);

    void insert(User insertUser);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

}
