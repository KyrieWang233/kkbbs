package com.kyriewang.kkbbs.mapper;

import com.kyriewang.kkbbs.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from user where id=#{id}")
    public User getUser(Long id);

    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{account_id},#{token},#{gmt_create},#{gmt_modified},#{avatar_url})")
    public void insertUser(User user);

    @Select("select * from user where token=#{token}")
    User findByToken(String token);

    @Select("select * from user where id=#{id}")
    User findById(Long id);

    @Select("select * from user where account_id=#{account_id}")
    User selectByaccountId(@Param(value="account_id") String account_id);

    @Update("update user set name=#{name},token=#{token},avatar_url=#{avatar_url},gmt_modified=#{gmt_modified} where id=#{id}")
    void updateGitUser(User user);
}
