package com.ruoyi.system.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.domain.ResponseEnum;
import com.ruoyi.common.core.page.ResponsePageInfo;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/config")
@Api(tags={"【参数配置 信息操作处理】Controller"})
public class SysConfigController extends BaseController
{
    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    @ApiOperation("查询参数配置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" ,dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , dataType = "int", paramType = "query", required = false),
    })
    public ResponsePageInfo<SysConfig> list(@ModelAttribute SysConfig config)
    {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return toResponsePageInfo(list);
    }

    @Log(title = "参数管理", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @GetMapping("/export")
    @ApiOperation("导出参数管理Excel")
    public Response<String> export(@ModelAttribute SysConfig config)
    {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        return util.exportExcel(list, "参数数据");
    }

    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    @ApiOperation("根据参数编号获取详细信息")
    public Response<SysConfig> getInfo(
            @ApiParam(name = "configId", value = "参数编号", required = true)
            @PathVariable("configId") Long configId
    ){
        return Response.success(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    @ApiOperation("根据参数键名查询参数值")
    public Response<String> getConfigKey(
            @ApiParam(name = "configKey", value = "参数键名", required = true)
            @PathVariable("configKey") String configKey
    ){
        return Response.success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
    @RepeatSubmit
    @ApiOperation("新增参数配置")
    public Response<Integer> add(
            @RequestBody @Validated  SysConfig config
    ){
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config)))
        {
            return Response.error(ResponseEnum.CONFIG_ADD_ERROR_EXIST);
        }
        config.setCreateBy(SecurityUtils.getUsername());
        return toResponse(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改参数配置")
    public Response<Integer> edit(
            @Validated @RequestBody SysConfig config
    ){
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config)))
        {
            return Response.error(ResponseEnum.CONFIG_UPDATE_ERROR_EXIST);
        }
        config.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{configIds}")
    @ApiOperation("删除参数配置")
    public Response<Integer> remove(
            @ApiParam(name = "configIds", value = "参数编号ids{逗号分隔}", required = true)
            @PathVariable Long[] configIds
    ){
        return toResponse(configService.deleteConfigByIds(configIds));
    }

    /**
     * 清空缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessTypeEnum.CLEAN)
    @DeleteMapping("/clearCache")
    @ApiOperation("清空缓存")
    public Response clearCache()
    {
        configService.clearCache();
        return Response.success();
    }
}
