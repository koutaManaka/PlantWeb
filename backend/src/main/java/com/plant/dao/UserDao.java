package com.plant.dao;

import com.plant.entity.UsersDTO;
import com.plant.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {
    @Select("select * from user where username = #{username};")
    User findByUsername(String username);

    @Insert("insert into user (username,password,email) values (#{username},#{password},#{email});")
    void register(User registerUser);

    @Select("select * from user where email = #{email};")
    User findByEmail(String email);

    @Update("update user set password = #{password} where username = #{username}")
    void change(User user);

    @Select("select * from user where id = #{id};")
    User findById(String id);

    @Select("select * from user where username like #{username} and email like #{email} and apiKey like #{apiKey} limit #{pageNum}, #{pageSize}")
    List<UsersDTO> selectPage(Integer pageNum, Integer pageSize,String username,String email,String apiKey);

    @Select("select count(*) from user where username like #{username} and email like #{email} and apiKey like #{apiKey}")
    Integer getTotal(String username,String email,String apiKey);

    @Update("update user set username = #{username}, role = #{role}, email = #{email}, apiKey = #{apiKey}, deviceId = #{deviceId} where id = #{id} ")
    void edit(User editUser);

    @Insert("insert into user (username,email,role,apiKey,deviceId) values (#{username},#{email},#{role},#{apiKey},#{deviceId});")
    void insert(User editUser);

    @Delete("delete from user where id = #{id}")
    void deleteById(@Param("id") Integer id);

    @Delete("<script>" +
            "DELETE FROM user WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    void deleteBatch(@Param("ids") List<Integer> ids);

}
