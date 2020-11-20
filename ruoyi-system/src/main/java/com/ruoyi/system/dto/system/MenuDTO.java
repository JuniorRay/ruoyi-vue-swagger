package com.ruoyi.system.dto.system;

import com.ruoyi.common.core.domain.TreeSelect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 【角色菜单】DTO
 *
 * @author JuniorRay
 * @date 2020-11-14
 */
@ApiModel(value = "MenuDTO", description = "角色菜单DTO")
@Data
public class MenuDTO {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "选中菜单列表")
    private List<Integer> checkedKeys;

    @ApiModelProperty(value = "下拉树结构列表")
    private List<TreeSelect> menus;

}
