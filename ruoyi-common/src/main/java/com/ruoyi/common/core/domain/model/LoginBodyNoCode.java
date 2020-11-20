package com.ruoyi.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户登录对象（免验证码）
 *
 * @author ruoyi
 */
@ApiModel(value = "LoginBodyNoCode", description = "用户登录对象（免验证码）")
@Data
public class LoginBodyNoCode
{
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    private String password;


}
