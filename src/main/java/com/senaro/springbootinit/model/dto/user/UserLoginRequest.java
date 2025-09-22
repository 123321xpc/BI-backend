package com.senaro.springbootinit.model.dto.user;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 用户登录请求
 *

 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @NotNull
    @Schema(description = "用户账号", example = "admin")
    private String userAccount;

    @NotNull
    @Schema(description = "用户密码", example = "123456")
    private String userPassword;
}
