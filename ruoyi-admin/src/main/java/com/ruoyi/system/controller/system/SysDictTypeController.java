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
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.page.ResponsePageInfo;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysDictTypeService;

/**
 * 数据字典类型信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dict/type")
@Api(tags={"【数据字典类型信息】Controller"})
public class SysDictTypeController extends BaseController
{
    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    @ApiOperation("查询数据字典类型信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" , paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , paramType = "query", required = false),

    })
    public ResponsePageInfo<SysDictType> list(@ModelAttribute SysDictType dictType)
    {
        startPage();
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return toResponsePageInfo(list);
    }

    @Log(title = "字典类型", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @GetMapping("/export")
    @ApiOperation("导出数据字典类型信息列表Excel")
    public Response<String> export(@ModelAttribute SysDictType dictType)
    {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        return util.exportExcel(list, "字典类型");
    }

    /**
     * 查询字典类型详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    @ApiOperation("查询字典类型详细")
    public Response<SysDictType> getInfo(
            @ApiParam(name = "dictId", value = "字典ID", required = true)
            @PathVariable("dictId") Long dictId
    ){
        return Response.success(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
    @ApiOperation("新增字典类型")
    public Response<Integer> add(@Validated @RequestBody SysDictType dict)
    {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict)))
        {
            return Response.error(ResponseEnum.DICTIONARY_ADD_ERROR_EXIST);
        }
        dict.setCreateBy(SecurityUtils.getUsername());
        return toResponse(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改字典类型")
    public Response<Integer> edit(@Validated @RequestBody SysDictType dict)
    {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict)))
        {
            return Response.error(ResponseEnum.DICTIONARY_UPDATE_ERROR_EXIST);
        }
        dict.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{dictIds}")
    @ApiOperation("删除字典类型")
    public Response<Integer> remove(
            @ApiParam(name = "dictIds", value = "字典Ids{逗号分隔}", required = true)
            @PathVariable Long[] dictIds
    ){
        return toResponse(dictTypeService.deleteDictTypeByIds(dictIds));
    }

    /**
     * 清空缓存
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessTypeEnum.CLEAN)
    @DeleteMapping("/clearCache")
    @ApiOperation("清空缓存")
    public Response clearCache()
    {
        dictTypeService.clearCache();
        return Response.success();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    @ApiOperation("获取字典选择框列表")
    public Response<List<SysDictType>> optionselect()
    {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return Response.success(dictTypes);
    }
}
