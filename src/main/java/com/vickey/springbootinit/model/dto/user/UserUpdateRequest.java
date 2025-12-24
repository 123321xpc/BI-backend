package com.vickey.springbootinit.model.dto.user;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 用户更新请求
 *

 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    @NotNull
    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "账号")
    private String userAvatar;

    @Schema(description = "用户简介")
    private String userProfile;

    @Schema(description = "权限")
    private String userRole;

    private static final long serialVersionUID = 1L;
}