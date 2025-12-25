package com.vickey.bi.model.vo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserVO implements Serializable {

    @Schema(description = "Id")
    @NotNull
    private Long id;


    @Schema(description = "昵称")
    @NotNull
    private String userName;

    @Schema(description = "头像")
    @NotNull
    private String userAvatar;

    @Schema(description = "介绍")
    @NotNull
    private String userProfile;

    @Schema(description = "权限")
    @NotNull
    private String userRole;

    @Schema(description = "创建时间")
    @NotNull
    private Date createTime;


    private static final long serialVersionUID = 1L;
}