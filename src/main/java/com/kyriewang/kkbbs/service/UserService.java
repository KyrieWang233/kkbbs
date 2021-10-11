package com.kyriewang.kkbbs.service;

import com.kyriewang.kkbbs.dto.GithubUser;
import com.kyriewang.kkbbs.dto.UserDto;
import com.kyriewang.kkbbs.model.User;

public interface UserService {

    User insertOrUpdate(GithubUser githubUser);

    User getById(long l);

    UserDto getuserById(long l);

    User getByName(String name);

    void register(String username, String password, String salt, String avatar_url);

    void updateUserInfo(User user);
}
