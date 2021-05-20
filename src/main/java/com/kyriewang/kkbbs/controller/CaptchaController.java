package com.kyriewang.kkbbs.controller;

import com.kyriewang.kkbbs.dto.ResultDto;
import com.kyriewang.kkbbs.util.StringRedisUtil;
import com.wf.captcha.SpecCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class CaptchaController {

    // 可以直接使用原生的spring的RedisTemplate来操作redis
    // @Autowired
    // private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private StringRedisUtil stringRedisUtil;

    @RequestMapping("/captcha")
    @ResponseBody
    public ResultDto captcha() throws Exception{

        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        // 存入redis并设置过期时间为30分钟
        stringRedisUtil.setEx(key, verCode, 5, TimeUnit.MINUTES);
        // 将key和base64返回给前端
        Map<String,String> map = new HashMap<>();
        map.put("key", key);
        map.put("image", specCaptcha.toBase64());
        return ResultDto.succ("查询验证码成功！",map);
    }
}
