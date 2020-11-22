package com.ruoyi.system.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【更新密码】DTO
 *
 * @author JuniorRay
 * @date 2020-11-14
 */
@ApiModel(value = "UpdatePwdDTO", description = "更新密码DTO")
@Data
public class UpdatePwdDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "旧密码")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    private String newPassword;
}
