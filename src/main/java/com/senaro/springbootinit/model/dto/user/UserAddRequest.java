package com.senaro.springbootinit.model.dto.user;

import java.io.Serializable;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 用户创建请求
 *

 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    @NotNull
    private String userName;

    /**
     * 账号
     */
    @NotNull
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色: user, admin
     */
    @NotNull
    private String userRole;

    private static final long serialVersionUID = 1L;
}