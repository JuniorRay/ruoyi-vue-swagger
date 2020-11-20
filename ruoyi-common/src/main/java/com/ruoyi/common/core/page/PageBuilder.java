package com.ruoyi.common.core.page;

import com.ruoyi.common.utils.ServletUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 表格数据处理 通过request请求 拦截获取PAGE_NUM，PAGE_SIZE，ORDER_BY_COLUMN，IS_ASC
 *
 * PageBuilder 表明是建造者模式
 * @author JuniorRay
 */
@ApiModel(value = "PageBuilder", description = "表格数据处理")
@Data
public class PageBuilder
{
    /**
     * 当前记录起始索引
     */
    @ApiModelProperty(value = "当前记录起始索引")
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    @ApiModelProperty(value = "每页显示记录数")
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    @ApiModelProperty(value = "排序列")
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    @ApiModelProperty(value = "排序的方向 \"desc\" 或者 \"asc\" ")
    public static final String IS_ASC = "isAsc";

    /**
     * 封装分页对象  抓取请求行中PAGE_NUM ，PAGE_SIZE，ORDER_BY_COLUMN，IS_ASC
     */
    private static PageBean getPageDomain()
    {
        PageBean pageBean = new PageBean();
        pageBean.setPageNum(ServletUtils.getParameterToInt(PAGE_NUM));
        pageBean.setPageSize(ServletUtils.getParameterToInt(PAGE_SIZE));
        pageBean.setOrderByColumn(ServletUtils.getParameter(ORDER_BY_COLUMN));
        pageBean.setIsAsc(ServletUtils.getParameter(IS_ASC));
        return pageBean;
    }

    public static PageBean buildPageRequest()
    {
        return getPageDomain();
    }
}
