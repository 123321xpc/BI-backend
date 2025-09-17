package com.senaro.springbootinit.model.dto.user;

import java.io.Serializable;
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
    private String userAccount;

    @NotNull
    private String userPassword;

    @NotNull
    private String checkPassword;
}
