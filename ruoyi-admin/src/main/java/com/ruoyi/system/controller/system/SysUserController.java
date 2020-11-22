package com.ruoyi.system.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.domain.ResponseEnum;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.ResponsePageInfo;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.dto.system.UserInfoDetailDTO;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user")
@Api(tags={"【用户信息】Controller"})
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    @ApiOperation("获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" ,dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , dataType = "int", paramType = "query", required = false),
    })
    public ResponsePageInfo<SysUser> list(@ModelAttribute SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return toResponsePageInfo(list);
    }

    @Log(title = "用户管理", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    @ApiOperation("导出用户列表Excel")
    public Response<String> export(@ModelAttribute SysUser user)
    {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessTypeEnum.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    @ApiOperation("导入用户列表Excel")
    public Response<String> importData(
            @ApiParam(name = "file", value = "二进制文件",required = true)
            @RequestBody MultipartFile file,
            @ApiParam(name = "updateSupport", value = "是否更新支持，如果已存在，则进行更新数据",required = true)
            @RequestBody boolean updateSupport
    ) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return Response.success(message);
    }

    @GetMapping("/importTemplate")
    @ApiOperation("导入用户模板")
    public Response<String> importTemplate()
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    @ApiOperation("根据用户编号获取详细信息")
    public Response<UserInfoDetailDTO> getInfo(
            @ApiParam(name = "userId", value = "用户编号", required = true)
            @PathVariable(value = "userId", required = false) Long userId
    ){
        UserInfoDetailDTO userInfoDetailDTO = new UserInfoDetailDTO();
        List<SysRole> roles = roleService.selectRoleAll();
        List<SysRole> rolesRet = SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList());
        userInfoDetailDTO.setRoles(rolesRet);
        userInfoDetailDTO.setPosts(postService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            userInfoDetailDTO.setUser(userService.selectUserById(userId));
            userInfoDetailDTO.setPostIds(postService.selectPostListByUserId(userId));
            userInfoDetailDTO.setRoleIds(roleService.selectRoleListByUserId(userId));
        }
        return Response.success(userInfoDetailDTO);
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
    @ApiOperation("新增用户")
    public Response<Integer> add(@Validated @RequestBody SysUser user)
    {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName())))
        {
            return Response.error(ResponseEnum.USER_ADD_ERROR_USERNAME);
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return Response.error(ResponseEnum.USER_ADD_ERROR_PHONE);
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return Response.error(ResponseEnum.USER_ADD_ERROR_MAIL);
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toResponse(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改用户")
    public Response<Integer> edit(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return Response.error(ResponseEnum.USER_UPDATE_ERROR_PHONE);
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return Response.error(ResponseEnum.USER_UPDATE_ERROR_MAIL);
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{userIds}")
    @ApiOperation("删除用户")
    public Response<Integer> remove(
            @ApiParam(name = "userIds", value = "用户编号ids{逗号分隔}", required = true)
            @PathVariable Long[] userIds
    ){
        return toResponse(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/resetPwd")
    @ApiOperation("重置密码")
    public Response<Integer> resetPwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/changeStatus")
    @ApiOperation("状态修改")
    public Response<Integer> changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(userService.updateUserStatus(user));
    }
}
