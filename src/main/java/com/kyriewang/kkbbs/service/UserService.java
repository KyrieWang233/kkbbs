package com.kyriewang.kkbbs.service;

import com.kyriewang.kkbbs.dto.GithubUser;
import com.kyriewang.kkbbs.mapper.UserMapper;
import com.kyriewang.kkbbs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User insertOrUpdate(GithubUser githubUser){
        User exist = userMapper.selectByaccountId(String.valueOf(githubUser.getId()));//查询数据库中是否有当前账户

        if(exist==null){
            User user = new User();//如果没有新建一个
            user.setName(githubUser.getName());//这三个是全都要更新的
            user.setAccount_id(String.valueOf(githubUser.getId()));
            user.setAvatar_url(githubUser.getAvatar_url());
            user.setToken(UUID.randomUUID().toString());
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            userMapper.insertUser(user);//如果不存在插入
            return user;
        }else{//如果存在
            exist.setName(githubUser.getName());//这四个是全都要更新的
            exist.setAvatar_url(githubUser.getAvatar_url());
            exist.setToken(UUID.randomUUID().toString());
            exist.setGmt_modified(System.currentTimeMillis());
            userMapper.updateGitUser(exist);
            return exist;
        }
    }
}
