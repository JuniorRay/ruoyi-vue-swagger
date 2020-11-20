package com.ruoyi.common.core.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.domain.ResponseEnum;
import com.ruoyi.common.core.page.PageBean;
import com.ruoyi.common.core.page.PageBuilder;
import com.ruoyi.common.core.page.ResponsePageInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author ruoyi
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据 PageHelper劫持sql查询实现分页
     */
    protected void startPage()
    {
        PageBean pageBean = PageBuilder.buildPageRequest();
        Integer pageNum = pageBean.getPageNum();
        Integer pageSize = pageBean.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageBean.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应请求分页数据 可以swagger识别
     * @param list 加入泛型任何list
     * @author JuniorRay
     */
    protected <T>ResponsePageInfo<T> toResponsePageInfo(List<T> list)
    {
        ResponsePageInfo<T> responsePageInfo = new ResponsePageInfo<T>();
        responsePageInfo.setCode(ResponseEnum.SUCCESS.getCode());
        responsePageInfo.setMsg("查询成功");
        responsePageInfo.setRows(list);
        responsePageInfo.setTotal(new PageInfo(list).getTotal());
        return responsePageInfo;
    }
    /**
     * 响应返回结果  可以swagger识别
     *
     * @param rows 影响行数
     * @author JuniorRay
     * @return 操作结果
     */
    protected Response<Integer> toResponse(int rows)
    {
        return rows > 0 ? Response.success(rows) : Response.error();
    }


    /**
     * 页面跳转
     */
    public String redirect(String url)
    {
        return StringUtils.format("redirect:{}", url);
    }
}
