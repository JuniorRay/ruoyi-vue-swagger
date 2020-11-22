package com.ruoyi.system.controller.system;

import java.util.List;
import java.util.Set;

import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.domain.model.LoginBodyNoCode;
import com.ruoyi.system.domain.dto.common.RouterDTO;
import com.ruoyi.system.domain.dto.system.TokenDTO;
import com.ruoyi.system.domain.dto.system.UserInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysMenuService;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
@Api(tags={"【登录验证】Controller"})
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录方法 （含验证码）
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    @ApiOperation("登录方法")
    public Response<TokenDTO> login(@RequestBody LoginBody loginBody)
    {   TokenDTO tokenDTO = new TokenDTO();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        tokenDTO.setToken(token);
        return Response.success(tokenDTO);
    }
    /**
     * 登录方法 （不含验证码）
     *
     * @param loginBodyNoCode 登录信息
     * @return 结果
     */
    @PostMapping("/login/noCode")
    @ApiOperation("登录方法（不含验证码）")
    public Response<TokenDTO> loginNoCode(@RequestBody LoginBodyNoCode loginBodyNoCode)
    {   TokenDTO tokenDTO = new TokenDTO();
        // 生成令牌
        String token = loginService.login(loginBodyNoCode.getUsername(), loginBodyNoCode.getPassword());
        tokenDTO.setToken(token);
        return Response.success(tokenDTO);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    @ApiOperation("获取用户信息")
    public Response<UserInfoDTO> getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUser(user);
        userInfoDTO.setRoles(roles);
        userInfoDTO.setPermissions(permissions);
        return Response.success(userInfoDTO);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    @ApiOperation("获取路由信息")
    public Response<List<RouterDTO>> getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return Response.success(menuService.buildMenus(menus));
    }
}
