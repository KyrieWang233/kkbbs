package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.AccessTokenDto;
import com.kyriewang.kkbbs.dto.GithubUser;
import com.kyriewang.kkbbs.mapper.UserMapper;
import com.kyriewang.kkbbs.model.User;
import com.kyriewang.kkbbs.provider.GithubProvider;
import com.kyriewang.kkbbs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {
    //自动装配，获得实例
    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Value("${github.client.id}")
    private String  client_id;

    @Value("${github.client.secret}")
    private String client_secret;

    @Value("${github.redirect.url}")
    private String redirect_url;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response)
    {
        AccessTokenDto accessTokenDto = new AccessTokenDto(
                client_id,
                client_secret,
                code,
                redirect_url,
                state);
        String token = githubProvider.getAccessToken(accessTokenDto);
        GithubUser githubUser = githubProvider.getUser(token);
        if(githubUser!=null){
            User user = userService.insertOrUpdate(githubUser);//根据githubUser查询
            request.getSession().setAttribute("user",user);
            response.addCookie(new Cookie("token",user.getToken()));
            return "redirect:/";
        }
        else{
            //登录失败
            log.error("callback get github user error,{}",githubUser);
            return "redirect:/";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");//删除session里面的内容
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);//设置存活时间为0
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }
}
