package com.ruoyi.system.dto.system;

import com.ruoyi.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【个人信息】DTO
 *
 * @author JuniorRay
 * @date 2020-11-11
 */
@ApiModel(value = "PersonInfoDTO", description = "个人信息DTO")
@Data
public class PersonInfoDTO {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "用户信息")
    private SysUser user;

    @ApiModelProperty(value = "所属角色组")
    private String roleGroup;

    @ApiModelProperty(value = "所属岗位组")
    private String postGroup;
}
