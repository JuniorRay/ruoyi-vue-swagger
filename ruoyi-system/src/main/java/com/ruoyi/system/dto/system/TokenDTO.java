package com.ruoyi.system.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【Token】DTO
 *
 * @author JuniorRay
 * @date 2020-11-14
 */
@ApiModel(value = "TokenDTO", description = "TokenDTO")
@Data
public class TokenDTO {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "令牌")
    private String token;
}
