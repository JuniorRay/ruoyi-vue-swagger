package com.ruoyi.common.core.page;

import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页对象实体
 *
 * @author ruoyi
 */
@ApiModel(value = "SysUser", description = "用户对象")
@Data
public class PageBean
{
    /** 当前记录起始索引 */
    @ApiModelProperty(value = "当前记录起始索引")
    private Integer pageNum;

    /** 每页显示记录数 */
    @ApiModelProperty(value = "每页显示记录数")
    private Integer pageSize;

    /** 排序列 */
    @ApiModelProperty(value = "排序列")
    private String orderByColumn;

    /** 排序的方向desc或者asc */
    @ApiModelProperty(value = "排序的方向desc或者asc")
    private String isAsc = "asc";

    public String getOrderBy()
    {
        if (StringUtils.isEmpty(orderByColumn))
        {
            return "";
        }
        return StringUtils.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    public Integer getPageNum()
    {
        return pageNum;
    }

    public void setPageNum(Integer pageNum)
    {
        this.pageNum = pageNum;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn()
    {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn)
    {
        this.orderByColumn = orderByColumn;
    }

    public String getIsAsc()
    {
        return isAsc;
    }

    public void setIsAsc(String isAsc)
    {
        this.isAsc = isAsc;
    }
}
