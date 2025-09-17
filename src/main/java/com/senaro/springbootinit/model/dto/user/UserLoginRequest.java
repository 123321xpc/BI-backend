package com.senaro.springbootinit.model.dto.user;

import java.io.Serializable;
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
    private String userAccount;

    @NotNull
    private String userPassword;
}
