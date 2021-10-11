package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.*;
import com.kyriewang.kkbbs.exception.CustomizeErrorCode;
import com.kyriewang.kkbbs.exception.CustomizerException;
import com.kyriewang.kkbbs.mapper.UserMapper;
import com.kyriewang.kkbbs.model.User;
import com.kyriewang.kkbbs.provider.GithubProvider;
import com.kyriewang.kkbbs.provider.QCloudProvider;
import com.kyriewang.kkbbs.service.UserService;
import com.kyriewang.kkbbs.shiro.AccountProfile;
import com.kyriewang.kkbbs.shiro.JwtUtils;
import com.kyriewang.kkbbs.util.SaltUtil;
import com.kyriewang.kkbbs.util.StringRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@PropertySource(value = { "classpath:user.properties" }, encoding = "utf-8")
@RestController
@Slf4j
public class AuthorizeController {
    //自动装配，获得实例
    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StringRedisUtil stringRedisUtil;

    @Autowired
    QCloudProvider qCloudProvider;

    @Value("${github.client.id}")
    private String  client_id;

    @Value("${github.client.secret}")
    private String client_secret;

    @Value("${github.redirect.url}")
    private String redirect_url;

    @RequestMapping("/callback")
    public ResultDto callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
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
        if(githubUser!=null&&githubUser.getLogin()!=null){

            User user = userService.insertOrUpdate(githubUser);//根据githubUser查询
            String jwt = jwtUtils.generateToken(user.getId());
            response.setHeader("Authorization", jwt);
            response.setHeader("Access-control-Expose-Headers", "Authorization");
            return ResultDto.succ("登录成功！",user);
        }
        else{
            //登录失败
            log.error("callback get github user error,{}",githubUser);
            return ResultDto.errorOf(CustomizeErrorCode.LOGIN_TIME_OUT);
        }
    }


    @RequiresAuthentication
    @GetMapping("/logout")
    public ResultDto logout(HttpServletRequest request) {
        SecurityUtils.getSubject().getSession().removeAttribute("userprofile");//删除session
        SecurityUtils.getSubject().logout();
        return ResultDto.succ("退出成功",null);
    }


    @CrossOrigin
    @PostMapping("/login")
    public ResultDto login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {

        System.out.println(userService.getClass());
        User user = userService.getByName(loginDto.getUsername());
        if(user==null){
            return ResultDto.errorOf(CustomizeErrorCode.USER_NOT_FOUND);
        }
        String password=loginDto.getPassword();
        Md5Hash md5Hash=new Md5Hash(password,user.getSalt(),1024);
        password=md5Hash.toHex();
        if(!password.equals(user.getPassword())){
            //user.setSalt("你还是看不到");
            return ResultDto.errorOf(CustomizeErrorCode.PASSWORD_ERROR);//密码错误需要返回
        }
        String jwt = jwtUtils.generateToken(user.getId());
        user.setPassword("你看不到的");
        user.setSalt("你还是看不到");
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        return ResultDto.succ("登录成功！",user);
    }


    //根据token获取用户信息
    @GetMapping("/login")
    public ResultDto login(){
        //从shiro认证里拿取用户信息
        AccountProfile userprofile = (AccountProfile)SecurityUtils.getSubject().getSession().getAttribute("userprofile");
        if(userprofile==null){
            return ResultDto.succ("用户未登陆",null);//这里知识查询一个信息，不需要报错
        }
        return ResultDto.succ("查询登录信息成功！",userprofile);
    }

    //登录接口
    @PostMapping("/register")
    public ResultDto register(@Validated @RequestBody RegisterDto registerDto){
        String verCode = registerDto.getVerCode();
        if(!registerDto.getPassword().equals(registerDto.getSecondPassword())){
            return ResultDto.fail(2013,"两次密码不一致");//这里防止前端拦截当成错误跳转，直接用与密码错误相同的错误码（当成用户表单错误）
        }
        String redisCode = stringRedisUtil.get(registerDto.getVerKey());

        if(redisCode==null||redisCode.equals("")){
            return ResultDto.fail(2013,"验证码已经过期");
        }

        if(verCode==null || !redisCode.equals(verCode.trim().toLowerCase())) {
            return ResultDto.fail(2013,"验证码不正确");
        }
        User exist = userService.getByName(registerDto.getUsername());//查询库中是否有相同的名称
        if(exist!=null){
            return ResultDto.fail(2013,"用户已经存在！");
        }
        String salt= SaltUtil.getSalt(5);
        Md5Hash md5Hash=new Md5Hash(registerDto.getPassword(),salt,1024);
        String password=md5Hash.toHex();
        String avatar_url="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
        userService.register(registerDto.getUsername(),password,salt,avatar_url);
        stringRedisUtil.delete(registerDto.getVerKey());
        return ResultDto.succ("注册成功！",null);
    }

    //修改个人信息
    @RequiresAuthentication
    @PostMapping("/userInformation")
    public ResultDto userInformation(MultipartFile image,Long id){
        MultipartFile file = image;
        Long len = file.getSize();
        double fileSize = (double) len / 1048576;
        if(fileSize>4){
            return ResultDto.errorOf(new CustomizerException(CustomizeErrorCode.UPLOAD_SIZE_ERROR));//如果文件上蹿过大
        }
        try{
            //对于用户头像，先删除原本的图片再添加进去新的头像
            qCloudProvider.deleteFileList(id.toString());
            String downloadurl = qCloudProvider.cloudUpload(file.getInputStream(),file.getContentType(),file.getOriginalFilename(),id.toString());
            User user = new User();
            user.setId(id);
            user.setAvatar_url(downloadurl);
            user.setGmt_modified(System.currentTimeMillis());
            userService.updateUserInfo(user);
            //修改session中的用户信息
            AccountProfile userprofile = (AccountProfile)SecurityUtils.getSubject().getSession().getAttribute("userprofile");
            userprofile.setAvatar_url(downloadurl);
            userprofile.setGmt_modified(System.currentTimeMillis());
            SecurityUtils.getSubject().getSession().setAttribute("userprofile",userprofile);
            return ResultDto.succ("修改成功！",user);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResultDto.fail("信息修改错误！");
        }


    }
}
