package com.kyriewang.kkbbs.service.impl;

import com.kyriewang.kkbbs.dto.GithubUser;
import com.kyriewang.kkbbs.dto.UserDto;
import com.kyriewang.kkbbs.mapper.UserMapper;
import com.kyriewang.kkbbs.model.User;
import com.kyriewang.kkbbs.service.UserService;
import com.kyriewang.kkbbs.shiro.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserMapper userMapper;


    public User insertOrUpdate(GithubUser githubUser){
        User exist = userMapper.selectByaccountId(String.valueOf(githubUser.getId()));//查询数据库中是否有当前账户

        //生成jwt

        if(exist==null){
            User sameName = userMapper.getByName(githubUser.getName());//查询库中是否有相同的名称,如果有那么修改名称
            if(sameName!=null){
                githubUser.setName(githubUser.getName()+"_github");
            }
            User user = new User();//如果没有新建一个
            user.setName(githubUser.getName());//这三个是全都要更新的
            user.setAccount_id(String.valueOf(githubUser.getId()));
            user.setAvatar_url(githubUser.getAvatar_url());
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            user.setRegister_way("github");
            userMapper.insertUser(user);//如果不存在插入
            user = userMapper.selectByaccountId(String.valueOf(githubUser.getId()));//查出当前账户的id
            return user;
        }else{//如果存在
            //exist.setName(githubUser.getName());//暂时不做改变
            exist.setAvatar_url(githubUser.getAvatar_url());
            exist.setGmt_modified(System.currentTimeMillis());
            userMapper.updateUserInfo(exist);
            return exist;
        }
    }

    public User getById(long l) {
        return userMapper.getUser(l);
    }

    public UserDto getuserById(long l){
        User user  = userMapper.getUser(l);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user,userDto);
        return userDto;
    }

    public User getByName(String name){
        return userMapper.getByName(name);
    }

    public void register(String username, String password, String salt, String avatar_url) {
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setSalt(salt);
        user.setAvatar_url(avatar_url);
        user.setGmt_create(System.currentTimeMillis());
        user.setGmt_modified(user.getGmt_create());
        user.setRegister_way("register");
        userMapper.insertUser(user);
    }

    public void updateUserInfo(User user){
        userMapper.updateUserInfo(user);
    }
}
