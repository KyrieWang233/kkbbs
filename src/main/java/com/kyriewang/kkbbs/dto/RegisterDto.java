package com.kyriewang.kkbbs.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class RegisterDto implements Serializable{
    @Size(min = 3,max = 25)
    @NotBlank(message = "昵称不能为空")
    private String username;

    @Size(min = 6,max = 35)
    @NotBlank(message = "密码不能为空")
    private String password;

    @Size(min=6,max=35)
    @NotBlank
    private String  secondPassword;

    @Size(min=5,max=5)
    @NotBlank
    private String verCode;

    @NotBlank
    private String verKey;
}
