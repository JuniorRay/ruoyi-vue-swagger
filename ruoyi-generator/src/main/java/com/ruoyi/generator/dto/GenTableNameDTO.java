package com.ruoyi.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【导入表名】DTO
 *
 * @author JuniorRay
 * @date 2020-11-14
 */
@ApiModel(value = "GenTableNameDTO", description = "导入表名DTO")
@Data
public class GenTableNameDTO {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "表名字符串（逗号分隔）")
    private String tables;

}
