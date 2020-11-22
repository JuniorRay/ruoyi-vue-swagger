package com.ruoyi.system.domain.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 路由配置信息 集成swagger
 *
 * @author JuniorRay
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "RouterDTO", description = "路由配置信息Vo")
public class RouterDTO
{
    /**
     * 路由名字
     */
    @ApiModelProperty(value = "路由名字")
    private String name;

    /**
     * 路由地址
     */
    @ApiModelProperty(value = "路由地址")
    private String path;

    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    @ApiModelProperty(value = "是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现")
    private boolean hidden;

    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    @ApiModelProperty(value = "重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击")
    private String redirect;

    /**
     * 组件地址
     */
    @ApiModelProperty(value = "组件地址")
    private String component;

    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     */
    @ApiModelProperty(value = "当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面")
    private Boolean alwaysShow;

    /**
     * 其他元素
     */
    @ApiModelProperty(value = "其他元素")
    private MetaDTO meta;

    /**
     * 子路由
     */
    @ApiModelProperty(value = "子路由")
    private List<RouterDTO> children;



    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public boolean getHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public String getRedirect()
    {
        return redirect;
    }

    public void setRedirect(String redirect)
    {
        this.redirect = redirect;
    }

    public String getComponent()
    {
        return component;
    }

    public void setComponent(String component)
    {
        this.component = component;
    }

    public Boolean getAlwaysShow()
    {
        return alwaysShow;
    }

    public void setAlwaysShow(Boolean alwaysShow)
    {
        this.alwaysShow = alwaysShow;
    }

    public MetaDTO getMeta()
    {
        return meta;
    }

    public void setMeta(MetaDTO meta)
    {
        this.meta = meta;
    }

    public List<RouterDTO> getChildren()
    {
        return children;
    }

    public void setChildren(List<RouterDTO> children)
    {
        this.children = children;
    }
}
