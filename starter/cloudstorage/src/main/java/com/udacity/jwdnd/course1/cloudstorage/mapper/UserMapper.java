package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    //create
    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES (#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int createUser(User user);

    //read
    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUserByUsername(String username);

    @Select("SELECT * FROM USERS WHERE userid = #{userId}")
    User getUserById(Integer userId);

//    //update
//    @Update("UPDATE user SET username=#{username}, password=#{password}, firstname=#{firstName}, lastname=#{lastName} WHERE userid=#{userId}")
//    User updateUser(User user);
//
//
//    //delete
//    @Delete("DELETE FROM USERS WHERE userid = #{userId}")
//    void deleteUserById(Integer userId);

}
