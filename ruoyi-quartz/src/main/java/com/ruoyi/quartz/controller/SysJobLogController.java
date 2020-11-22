package com.ruoyi.quartz.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.page.ResponsePageInfo;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.quartz.domain.SysJobLog;
import com.ruoyi.quartz.service.ISysJobLogService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度日志操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/jobLog")
@Api(tags={"【调度日志操作处理】Controller"})
public class SysJobLogController extends BaseController
{
    @Autowired
    private ISysJobLogService jobLogService;

    /**
     * 查询定时任务调度日志列表
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:list')")
    @GetMapping("/list")
    @ApiOperation("查询定时任务调度日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" ,dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , dataType = "int", paramType = "query", required = false),
    })
    public ResponsePageInfo<SysJobLog> list(@ModelAttribute SysJobLog sysJobLog)
    {
        startPage();
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        return toResponsePageInfo(list);
    }

    /**
     * 导出定时任务调度日志列表
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:export')")
    @Log(title = "任务调度日志", businessType = BusinessTypeEnum.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出定时任务调度日志列表Excel")
    public Response<String> export(@ModelAttribute SysJobLog sysJobLog)
    {
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        ExcelUtil<SysJobLog> util = new ExcelUtil<SysJobLog>(SysJobLog.class);
        return util.exportExcel(list, "调度日志");
    }

    /**
     * 根据调度编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:query')")
    @GetMapping(value = "/{configId}")
    @ApiOperation("根据调度编号获取详细信息")
    public Response<SysJobLog> getInfo(
            @ApiParam(name = "jobLogId", value = "调度编号ids", required = true)
            @PathVariable("jobLogId") Long jobLogId
    ){
        return Response.success(jobLogService.selectJobLogById(jobLogId));
    }


    /**
     * 删除定时任务调度日志
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:remove')")
    @Log(title = "定时任务调度日志", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{jobLogIds}")
    @ApiOperation("删除定时任务调度日志")
    public Response<Integer> remove(
            @ApiParam(name = "jobLogIds", value = "调度编号ids{逗号分隔}", required = true)
            @PathVariable Long[] jobLogIds
    ){
        return toResponse(jobLogService.deleteJobLogByIds(jobLogIds));
    }

    /**
     * 清空定时任务调度日志
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:remove')")
    @Log(title = "调度日志", businessType = BusinessTypeEnum.CLEAN)
    @DeleteMapping("/clean")
    @ApiOperation("清空定时任务调度日志")
    public Response clean()
    {
        jobLogService.cleanJobLog();
        return Response.success();
    }
}
