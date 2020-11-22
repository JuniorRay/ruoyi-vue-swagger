package com.ruoyi.system.controller.system;

import java.util.List;

import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.domain.ResponseEnum;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.ResponsePageInfo;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/role")
@Api(tags={"【角色信息】Controller"})
public class SysRoleController extends BaseController
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService userService;

    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    @ApiOperation("查询角色信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" , paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , paramType = "query", required = false),

    })
    public ResponsePageInfo<SysRole> list(@ModelAttribute SysRole role)
    {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return toResponsePageInfo(list);
    }

    @Log(title = "角色管理", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:role:export')")
    @GetMapping("/export")
    @ApiOperation("导出角色信息列表Excel")
    public Response<String> export(@ModelAttribute SysRole role)
    {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        return util.exportExcel(list, "角色数据");
    }

    /**
     * 根据角色编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    @ApiOperation("根据角色编号获取详细信息")
    public Response<SysRole> getInfo(
            @ApiParam(name = "roleId", value = "角色编号", required = true)
            @PathVariable("roleId") Long roleId
    ){
        return Response.success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
    @ApiOperation("新增角色")
    public Response<Integer> add(@Validated @RequestBody SysRole role)
    {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role)))
        {
            return Response.error(ResponseEnum.ROLE_ADD_ERROR_EXIST_NAME);
        }
        else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role)))
        {
            return Response.error(ResponseEnum.ROLE_ADD_ERROR_EXIST_AUTHORITY);
        }
        role.setCreateBy(SecurityUtils.getUsername());
        return toResponse(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改保存角色")
    public Response edit(@Validated @RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role)))
        {
            return Response.error(ResponseEnum.ROLE_UPDATE_ERROR_EXIST_NAME);
        }
        else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role)))
        {
            return Response.error(ResponseEnum.ROLE_UPDATE_ERROR_EXIST_AUTHORITY);
        }
        role.setUpdateBy(SecurityUtils.getUsername());

        if (roleService.updateRole(role) > 0)
        {
            // 更新缓存用户权限
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin())
            {
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
                tokenService.setLoginUser(loginUser);
            }
            return Response.success();
        }
        return Response.error(ResponseEnum.ROLE_UPDATE_ERROR);
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/dataScope")
    @ApiOperation("修改保存数据权限")
    public Response<Integer> dataScope(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        return toResponse(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/changeStatus")
    @ApiOperation("状态修改")
    public Response<Integer> changeStatus(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{roleIds}")
    @ApiOperation("删除角色")
    public Response<Integer> remove(
            @ApiParam(name = "roleIds", value = "角色编号ids{逗号分隔}", required = true)
            @PathVariable Long[] roleIds
    ){
        return toResponse(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    @ApiOperation("获取角色选择框列表")
    public Response<List<SysRole>> optionselect()
    {
        return Response.success(roleService.selectRoleAll());
    }
}
