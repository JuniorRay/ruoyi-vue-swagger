package com.ruoyi.system.dto.system;

import com.ruoyi.common.core.domain.TreeSelect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 【角色部门列表】DTO
 *
 * @author JuniorRay
 * @date 2020-11-14
 */
@ApiModel(value = "RoleDeptDTO", description = "角色部门列表DTO")
@Data
public class RoleDeptDTO {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "选中菜单列表")
    private List<Integer> checkedKeys;

    @ApiModelProperty(value = "下拉树结构列表")
    private List<TreeSelect> depts;

}
