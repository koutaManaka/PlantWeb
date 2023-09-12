package com.plant.dao;

import com.plant.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {
    @Select("select * from user where username = #{username};")
    User findByUsername(String username);

    @Insert("insert into user (username,password,email) values (#{username},#{password},#{email});")
    void register(User registerUser);

    @Select("select  * from user where email = #{email};")
    User findByEmail(String email);

    @Update("update user set password = #{password} where username = ")
    void change(User user);
}
