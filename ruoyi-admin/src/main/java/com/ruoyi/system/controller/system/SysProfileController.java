package com.ruoyi.system.controller.system;

import java.io.IOException;

import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.domain.ResponseEnum;
import com.ruoyi.system.dto.system.PersonInfoDTO;
import com.ruoyi.system.dto.system.UpdatePwdDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 个人信息 业务处理
 * 修改识别 swagger
 * @author JuniorRay
 */
@RestController
@RequestMapping("/system/user/profile")
@Api(tags={"【个人信息业务处理】Controller"})
public class SysProfileController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * 个人信息
     */
    @GetMapping
    @ApiOperation("个人信息")
    public Response<PersonInfoDTO> profile()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        PersonInfoDTO personInfoDTO = new  PersonInfoDTO();
        personInfoDTO.setUser(user);
        personInfoDTO.setPostGroup(userService.selectUserPostGroup(loginUser.getUsername()));
        personInfoDTO.setRoleGroup(userService.selectUserRoleGroup(loginUser.getUsername()));
        return  Response.success(personInfoDTO);
    }


    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改用户")
    public Response updateProfile(@RequestBody SysUser user)
    {
        if (userService.updateUserProfile(user) > 0)
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            // 更新缓存用户信息
            loginUser.getUser().setNickName(user.getNickName());
            loginUser.getUser().setPhonenumber(user.getPhonenumber());
            loginUser.getUser().setEmail(user.getEmail());
            loginUser.getUser().setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
            return Response.success();
        }
        return Response.error(ResponseEnum.USER_UPDATE_ERROR);
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/updatePwd")
    @ApiOperation("重置密码")
    public Response updatePwd(@RequestBody UpdatePwdDTO updatePwdDTO) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(updatePwdDTO.getOldPassword(), password))
        {
            return Response.error(ResponseEnum.UPDATE_PASSWORD_ERROR_OLD_ERROR);
        }
        if (SecurityUtils.matchesPassword(updatePwdDTO.getNewPassword(), password))
        {
            return Response.error(ResponseEnum.UPDATE_PASSWORD_ERROR_OLD_REPEAT);
        }
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(updatePwdDTO.getNewPassword())) > 0)
        {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(updatePwdDTO.getNewPassword()));
            tokenService.setLoginUser(loginUser);
            return Response.success();
        }
        return Response.error(ResponseEnum.UPDATE_PASSWORD_ERROR);
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/avatar")
    @ApiOperation("头像上传")
    public Response<String> avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar))
            {
                Response response = Response.success(avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return response;
            }
        }
        return Response.error(ResponseEnum.ABNORMAL_PICTURE_UPLOAD);
    }
}
