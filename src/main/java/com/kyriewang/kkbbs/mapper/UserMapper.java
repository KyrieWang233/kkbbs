package com.kyriewang.kkbbs.mapper;

import com.kyriewang.kkbbs.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from user where id=#{id}")
    User getUser(Long id);

    @Select("select * from user where name=#{name}")
    User getByName(String name);

    @Insert("insert into user (name,account_id,password,salt,gmt_create,gmt_modified,role,register_way,avatar_url) values (#{name},#{account_id},#{password},#{salt},#{gmt_create},#{gmt_modified},#{role},#{register_way},#{avatar_url})")
    void insertUser(User user);


    @Select("select * from user where id=#{id}")
    User findById(Long id);

    @Select("select * from user where account_id=#{account_id}")
    User selectByaccountId(@Param(value="account_id") String account_id);

    @Update("update user set avatar_url=#{avatar_url},gmt_modified=#{gmt_modified} where id=#{id}")
    void updateUserInfo(User user);



}
