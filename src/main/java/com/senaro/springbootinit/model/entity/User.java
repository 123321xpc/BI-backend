package com.senaro.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * @TableName user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {


    @Schema(description = "id")
    @NotNull
    private Long id;


    @Schema(description = "账号")
    @NotNull
    private String userAccount;


    @Schema(description = "密码")
    @NotNull
    private String userPassword;


    @Schema(description = "微信开放平台id")
    @NotNull
    private String unionId;


    @Schema(description = "公众号openId")
    @NotNull
    private String mpOpenId;


    @Schema(description = "用户昵称")
    @NotNull
    private String userName;


    @Schema(description = "用户头像")
    @NotNull
    private String userAvatar;


    @Schema(description = "用户简介")
    @NotNull
    private String userProfile;


    @Schema(description = "用户角色：user/admin/ban")
    @NotNull
    private String userRole;


    @Schema(description = "编辑时间")
    @NotNull
    private Date editTime;


    @Schema(description = "创建时间")
    @NotNull
    private Date createTime;


    @Schema(description = "更新时间")
    @NotNull
    private Date updateTime;


    @Schema(description = "是否删除")
    @NotNull
    @TableLogic
    private Integer isDelete;


}
