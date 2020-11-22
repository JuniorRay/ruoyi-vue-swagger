package com.ruoyi.system.controller.system;

import java.util.List;

import com.ruoyi.common.core.domain.ResponseEnum;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.system.dto.system.MenuDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysMenuService;

/**
 * 菜单信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/menu")
@Api(tags={"【菜单信息】Controller"})
public class SysMenuController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    @ApiOperation("获取菜单列表")
    public Response<List<SysMenu>> list(@ModelAttribute SysMenu menu)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return Response.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    @ApiOperation("根据菜单编号获取详细信息")
    public Response<SysMenu> getInfo(
            @ApiParam(name = "menuId", value = "菜单编号", required = true)
            @PathVariable("menuId") Long menuId
    ){
        return Response.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    @ApiOperation("获取菜单下拉树列表")
    public Response<List<TreeSelect>> treeselect(@ModelAttribute SysMenu menu)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return Response.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    @ApiOperation("加载对应角色菜单列表树")
    public Response<MenuDTO> roleMenuTreeselect(
            @ApiParam(name = "roleId", value = "角色ID", required = true)
            @PathVariable("roleId") Long roleId
    ){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        List<SysMenu> menus = menuService.selectMenuList(loginUser.getUser().getUserId());
        MenuDTO menuDTO= new MenuDTO();
        menuDTO.setCheckedKeys(menuService.selectMenuListByRoleId(roleId));
        menuDTO.setMenus(menuService.buildMenuTreeSelect(menus));
        return Response.success(menuDTO);
    }

    /**
     * 新增菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "菜单管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
    @ApiOperation("新增菜单")
    public Response<Integer> add(@Validated @RequestBody SysMenu menu)
    {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return Response.error(ResponseEnum.MENU_ADD_ERROR_EXIST);
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame())
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS))
        {
            return Response.error(ResponseEnum.MENU_ADD_ERROR_HTTP);
        }
        menu.setCreateBy(SecurityUtils.getUsername());
        return toResponse(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "菜单管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改菜单")
    public Response<Integer> edit(@Validated @RequestBody SysMenu menu)
    {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return Response.error(ResponseEnum.MENU_UPDATE_ERROR_EXIST);
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame())
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS))
        {
            return Response.error(ResponseEnum.MENU_UPDATE_ERROR_HTTP);
        }
        else if (menu.getMenuId().equals(menu.getParentId()))
        {
            return Response.error(ResponseEnum.MENU_UPDATE_ERROR_SELF);
        }
        menu.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "菜单管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{menuId}")
    @ApiOperation("删除菜单")
    public Response<Integer> remove(
            @ApiParam(name = "menuId", value = "菜单ID", required = true)
            @PathVariable("menuId") Long menuId
    ){
        if (menuService.hasChildByMenuId(menuId))
        {
            return Response.error(ResponseEnum.MENU_DELETE_ERROR_SUB);
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return Response.error(ResponseEnum.MENU_DELETE_ERROR_ROLE);
        }
        return toResponse(menuService.deleteMenuById(menuId));
    }
}
