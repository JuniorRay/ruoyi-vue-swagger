package com.ruoyi.system.controller.monitor;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.page.ResponsePageInfo;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.service.ISysLogininforService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统访问记录
 * 添加Response swagger识别
 * @author JuniorRay
 */
@RestController
@RequestMapping("/monitor/logininfor")
@Api(tags={"【系统访问记录】Controller"})
public class SysLogininforController extends BaseController
{
    @Autowired
    private ISysLogininforService logininforService;

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
    @GetMapping("/list")
    @ApiOperation("查询登录日志信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" ,dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , dataType = "int", paramType = "query", required = false),
    })
    public ResponsePageInfo<SysLogininfor> list(@ModelAttribute SysLogininfor logininfor)
    {
        startPage();
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        return toResponsePageInfo(list);
    }

    @Log(title = "登录日志", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:export')")
    @GetMapping("/export")
    @ApiOperation("导出登录日志Excel")
    public Response<String> export(@ModelAttribute SysLogininfor logininfor)
    {
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
        return util.exportExcel(list, "登录日志");
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{infoIds}")
    @ApiOperation("删除登录日志")
    public Response<Integer> remove(
            @ApiParam(name = "infoIds", value = "登录日志ids{逗号分隔}", required = true)
            @PathVariable Long[] infoIds
    )
    {
        return toResponse(logininforService.deleteLogininforByIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessTypeEnum.CLEAN)
    @DeleteMapping("/clean")
    @ApiOperation("清除登录日志")
    public Response clean()
    {
        logininforService.cleanLogininfor();
        return Response.success();
    }
}
