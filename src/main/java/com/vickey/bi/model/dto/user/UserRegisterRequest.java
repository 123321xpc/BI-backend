package com.vickey.bi.model.dto.user;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 用户注册请求体
 *

 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @NotNull
    @Schema(description = "用户账号", example = "admin")
    private String userAccount;

    @NotNull
    @Schema(description = "用户密码", example = "123456")
    private String userPassword;

    @NotNull
    @Schema(description = "确认密码", example = "123456")
    private String checkPassword;
}
