package com.ruoyi.system.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.page.ResponsePageInfo;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysDictTypeService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dict/data")
@Api(tags={"【数据字典信息】Controller"})
public class SysDictDataController extends BaseController
{
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    @ApiOperation("查询数据字典信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" , paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , paramType = "query", required = false),

    })
    public ResponsePageInfo<SysDictData> list(@ModelAttribute SysDictData dictData)
    {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return toResponsePageInfo(list);
    }

    @Log(title = "字典数据", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @GetMapping("/export")
    @ApiOperation("导出数据字典信息列表Excel")
    public Response<String> export(@ModelAttribute SysDictData dictData)
    {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        return util.exportExcel(list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    @ApiOperation("查询字典数据详细")
    public Response<SysDictData> getInfo(
            @ApiParam(name = "dictCode", value = "字典编号", required = true)
            @PathVariable("dictCode") Long dictCode
    ){
        return Response.success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    @ApiOperation("根据字典类型查询字典数据信息")
    public Response<List<SysDictData>> dictType(
            @ApiParam(name = "dictType", value = "根据字典类型查询字典数据信息", required = true)
            @PathVariable("dictType") String dictType
    ){
        return Response.success(dictTypeService.selectDictDataByType(dictType));
    }

    /**
     * 新增字典数据
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
    @ApiOperation("新增字典数据")
    public Response<Integer> add(@Validated @RequestBody SysDictData dict)
    {
        dict.setCreateBy(SecurityUtils.getUsername());
        return toResponse(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典数据
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改保存字典数据")
    public Response<Integer> edit(@Validated @RequestBody SysDictData dict)
    {
        dict.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典数据
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典数据", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{dictCodes}")
    @ApiOperation("删除字典数据")
    public Response<Integer> remove(
            @ApiParam(name = "dictCodes", value = "字典编号codes{逗号分隔}", required = true)
            @PathVariable Long[] dictCodes
    ){
        return toResponse(dictDataService.deleteDictDataByIds(dictCodes));
    }
}
