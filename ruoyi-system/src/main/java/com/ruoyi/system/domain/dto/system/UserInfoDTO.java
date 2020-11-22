package com.ruoyi.system.domain.dto.system;

import com.ruoyi.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * 【用户信息】DTO
 *
 * @author JuniorRay
 * @date 2020-11-11
 */
@ApiModel(value = "UserInfoDTO", description = "用户信息DTO")
@Data
public class UserInfoDTO {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "用户信息")
    private SysUser user;

    @ApiModelProperty(value = "角色集合")
    private Set<String> roles;

    @ApiModelProperty(value = "权限集合")
    private Set<String> permissions;
}
