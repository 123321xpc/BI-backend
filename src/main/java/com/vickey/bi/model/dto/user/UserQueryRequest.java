package com.vickey.bi.model.dto.user;

import com.vickey.bi.common.PageRequest;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询请求
 *

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    @Schema(description = "昵称")
    private String userName;

    @Schema(description = "介绍")
    private String userProfile;

    @Schema(description = "权限")
    private String userRole;

    private static final long serialVersionUID = 1L;
}