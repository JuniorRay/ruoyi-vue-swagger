package com.ruoyi.system.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【验证码】DTO
 *
 * @author JuniorRay
 * @date 2020-11-14
 */
@ApiModel(value = "GenTableInfoDTO", description = "验证码DTO")
@Data
public class CaptchaImageDTO {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "验证码信息uuid")
    private String uuid;

    @ApiModelProperty(value = "Base64图片")
    private String img;

}
