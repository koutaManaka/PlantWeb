package com.plant.pojo;


import lombok.Data;

@Data
public class User {
    private Integer id; // 主键
    private String gender; // 性别
    private String username; // 用户名，唯一
    private String password; // 密码
    private String address; // 地址
    private String telephone; // 联系电话
    private String email; //邮箱
}
