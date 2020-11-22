package com.ruoyi.system.controller.monitor;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.page.ResponsePageInfo;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.service.ISysOperLogService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 * 添加Response swagger识别
 * @author JuniorRay
 */
@RestController
@RequestMapping("/monitor/operlog")
@Api(tags={"【操作日志记录】Controller"})
public class SysOperlogController extends BaseController
{
    @Autowired
    private ISysOperLogService operLogService;

    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @GetMapping("/list")
    @ApiOperation("查询操作日志记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" ,dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , dataType = "int", paramType = "query", required = false),
    })
    public ResponsePageInfo<SysOperLog> list(@ModelAttribute SysOperLog operLog)
    {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return toResponsePageInfo(list);
    }

    @Log(title = "操作日志", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
    @GetMapping("/export")
    @ApiOperation("导出操作日志Excel")
    public Response<String> export(@ModelAttribute SysOperLog operLog)
    {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        return util.exportExcel(list, "操作日志");
    }

    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    @ApiOperation("删除操作日志")
    public Response<Integer> remove(
            @ApiParam(name = "infoIds", value = "操作日志ids{逗号分隔}", required = true)
            @PathVariable Long[] operIds
    ) {
        return toResponse(operLogService.deleteOperLogByIds(operIds));
    }

    @Log(title = "操作日志", businessType = BusinessTypeEnum.CLEAN)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    @ApiOperation("清除操作日志")
    public Response clean()
    {
        operLogService.cleanOperLog();
        return Response.success();
    }
}
