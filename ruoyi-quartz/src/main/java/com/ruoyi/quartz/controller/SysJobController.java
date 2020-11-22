package com.ruoyi.quartz.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.domain.ResponseEnum;
import com.ruoyi.common.core.page.ResponsePageInfo;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.service.ISysJobService;
import com.ruoyi.quartz.util.CronUtils;
import io.swagger.annotations.*;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度任务信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/job")
@Api(tags={"【调度任务信息操作处理】Controller"})
public class SysJobController extends BaseController
{
    @Autowired
    private ISysJobService jobService;

    /**
     * 查询定时任务列表
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:list')")
    @GetMapping("/list")
    @ApiOperation("查询定时任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" ,dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , dataType = "int", paramType = "query", required = false),
    })
    public ResponsePageInfo<SysJob> list(@ModelAttribute SysJob sysJob)
    {
        startPage();
        List<SysJob> list = jobService.selectJobList(sysJob);
        return toResponsePageInfo(list);
    }

    /**
     * 导出定时任务列表
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:export')")
    @Log(title = "定时任务", businessType = BusinessTypeEnum.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出定时任务列表Excel")
    public Response<String> export(@ModelAttribute SysJob sysJob)
    {
        List<SysJob> list = jobService.selectJobList(sysJob);
        ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
        return util.exportExcel(list, "定时任务");
    }

    /**
     * 获取定时任务详细信息
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:query')")
    @GetMapping(value = "/{jobId}")
    @ApiOperation("获取定时任务详细信息")
    public Response<SysJob> getInfo(
            @ApiParam(name = "jobId", value = "定时任务id", required = true)
            @PathVariable("jobId") Long jobId
    ){
        return Response.success(jobService.selectJobById(jobId));
    }

    /**
     * 新增定时任务
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:add')")
    @Log(title = "定时任务", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
    @ApiOperation("新增定时任务")
    public Response<Integer> add(@RequestBody SysJob sysJob) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(sysJob.getCronExpression()))
        {
            return Response.error(ResponseEnum.CRON_ERROR);
        }
        sysJob.setCreateBy(SecurityUtils.getUsername());
        return toResponse(jobService.insertJob(sysJob));
    }

    /**
     * 修改定时任务
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:edit')")
    @Log(title = "定时任务", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改定时任务")
    public Response<Integer> edit(@RequestBody SysJob sysJob) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(sysJob.getCronExpression()))
        {
            return Response.error(ResponseEnum.CRON_ERROR);
        }
        sysJob.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(jobService.updateJob(sysJob));
    }

    /**
     * 定时任务状态修改
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title = "定时任务", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/changeStatus")
    @ApiOperation("定时任务状态修改")
    public Response<Integer> changeStatus(@RequestBody SysJob job) throws SchedulerException
    {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toResponse(jobService.changeStatus(newJob));
    }

    /**
     * 定时任务立即执行一次
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title = "定时任务", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/run")
    @ApiOperation("定时任务立即执行一次")
    public Response run(@RequestBody SysJob job) throws SchedulerException
    {
        jobService.run(job);
        return Response.success();
    }

    /**
     * 删除定时任务
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:remove')")
    @Log(title = "定时任务", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{jobIds}")
    @ApiOperation("删除定时任务")
    public Response remove(
            @ApiParam(name = "jobIds", value = "定时任务ids{逗号分隔}", required = true)
            @PathVariable Long[] jobIds
    ) throws SchedulerException, TaskException{
        jobService.deleteJobByIds(jobIds);
        return Response.success();
    }
}
