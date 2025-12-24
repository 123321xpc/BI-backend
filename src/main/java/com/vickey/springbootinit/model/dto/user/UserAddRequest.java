package com.vickey.springbootinit.model.dto.user;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 用户创建请求
 *

 */
@Data
public class UserAddRequest implements Serializable {

    @NotNull
    @Schema(description = "昵称")
    private String userName;

    @NotNull
    @Schema(description = "账号")
    private String userAccount;

    @Schema(description = "头像")
    private String userAvatar;

    @NotNull
    @Schema(description = "权限")
    private String userRole;

    private static final long serialVersionUID = 1L;
}