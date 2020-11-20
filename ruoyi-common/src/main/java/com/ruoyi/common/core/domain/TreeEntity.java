package com.ruoyi.common.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree基类
 *
 * @author ruoyi
 */
@ApiModel(value = "TreeEntity", description = "Tree基类")
@Data
public class TreeEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 父菜单名称 */
    @ApiModelProperty(value = "父菜单名称")
    private String parentName;

    /** 父菜单ID */
    @ApiModelProperty(value = "父菜单ID")
    private Long parentId;

    /** 显示顺序 */
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;

    /** 祖级列表 */
    @ApiModelProperty(value = "祖级列表")
    private String ancestors;

    /** 子部门 */
    @ApiModelProperty(value = "子部门")
    private List<?> children = new ArrayList<>();

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    public List<?> getChildren()
    {
        return children;
    }

    public void setChildren(List<?> children)
    {
        this.children = children;
    }
}
