package com.ruoyi.system.domain.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【通用上传】DTO
 *
 * @author JuniorRay
 * @date 2020-11-14
 */
@ApiModel(value = "UploadFileDTO", description = "通用上传DTO")
@Data
public class UploadFileDTO {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "新文件名称")
    private String fileName;

    @ApiModelProperty(value = "保存地址")
    private String url;

}
