package com.ruoyi.generator.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.page.ResponsePageInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.generator.domain.GenTable;
import com.ruoyi.generator.domain.GenTableColumn;
import com.ruoyi.generator.dto.GenTableInfoDTO;
import com.ruoyi.generator.dto.GenTableNameDTO;
import com.ruoyi.generator.service.IGenTableColumnService;
import com.ruoyi.generator.service.IGenTableService;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 代码生成 操作处理
 *
 * @author JuniorRay
 */
@RestController
@RequestMapping("/tool/gen")
@Api(tags={"【代码生成 操作处理】Controller"})
public class GenController extends BaseController
{
    @Autowired
    private IGenTableService genTableService;

    @Autowired
    private IGenTableColumnService genTableColumnService;

    /**
     * 查询代码生成列表
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @GetMapping("/list")
    @ApiOperation("查询代码生成列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" ,dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , dataType = "int", paramType = "query", required = false),
    })
    public ResponsePageInfo<GenTable> genList(@ModelAttribute GenTable genTable)
    {
        startPage();
        List<GenTable> list = genTableService.selectGenTableList(genTable);
        return toResponsePageInfo(list);
    }

    /**
     * 修改代码生成业务
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:query')")
    @GetMapping(value = "/{talbleId}")
    @ApiOperation("修改代码生成业务")
    public Response<GenTableInfoDTO> getInfo(
            @ApiParam(name = "talbleId", value = "表业务ID", required = true)
            @PathVariable("talbleId") Long talbleId
    ){
        GenTable table = genTableService.selectGenTableById(talbleId);
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(talbleId);

        GenTableInfoDTO genTableInfoDTO = new GenTableInfoDTO();
        genTableInfoDTO.setInfo(table);
        genTableInfoDTO.setRows(list);
        return Response.success(genTableInfoDTO);
    }

    /**
     * 查询数据库列表
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @GetMapping("/db/list")
    @ApiOperation("查询数据库列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" ,dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , dataType = "int", paramType = "query", required = false),
    })
    public ResponsePageInfo<GenTable> dataList(@ModelAttribute GenTable genTable)
    {
        startPage();
        List<GenTable> list = genTableService.selectDbTableList(genTable);
        return toResponsePageInfo(list);
    }

    /**
     * 查询数据表字段列表
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @GetMapping(value = "/column/{talbleId}")
    @ApiOperation("查询数据表字段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" ,dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , dataType = "int", paramType = "query", required = false),
    })
    public ResponsePageInfo<GenTableColumn> columnList(
            @ApiParam(name = "tableId", value = "业务表id")
            @RequestParam(name = "tableId", required = true) Long tableId
    ){
        ResponsePageInfo dataInfo = new ResponsePageInfo();
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        dataInfo.setRows(list);
        dataInfo.setTotal(list.size());
        return dataInfo;
    }

    /**
     * 导入表结构（保存）
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @Log(title = "代码生成", businessType = BusinessTypeEnum.IMPORT)
    @PostMapping("/importTable")
    @ApiOperation("导入表结构（保存）")
    public Response importTableSave(
            @ApiParam(name = "tables", value = "业务表名称tableNames")
            @RequestBody GenTableNameDTO genTableNameDTO
    ){
        String[] tableNames = Convert.toStrArray(genTableNameDTO.getTables());
        // 查询表信息
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
        genTableService.importGenTable(tableList);
        return Response.success();
    }

    /**
     * 修改保存代码生成业务
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:edit')")
    @Log(title = "代码生成", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改保存代码生成业务")
    public Response editSave(@Validated @RequestBody GenTable genTable)
    {
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
        return Response.success();
    }

    /**
     * 删除代码生成
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:remove')")
    @Log(title = "代码生成", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{tableIds}")
    @ApiOperation("删除代码生成")
    public Response remove(
            @ApiParam(name = "talbleIds", value = "表业务ids{逗号分隔}", required = true)
            @PathVariable Long[] tableIds
    ){
        genTableService.deleteGenTableByIds(tableIds);
        return Response.success();
    }

    /**
     * 预览代码
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:preview')")
    @GetMapping("/preview/{tableId}")
    @ApiOperation("预览代码")
    public Response<Map<String, String>> preview(
            @ApiParam(name = "talbleId", value = "表业务id", required = true)
            @PathVariable("tableId") Long tableId
    ) throws IOException {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return Response.success(dataMap);
    }

    /**
     * 生成代码（下载方式）
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @Log(title = "代码生成", businessType = BusinessTypeEnum.GENCODE)
    @GetMapping("/download/{tableName}")
    @ApiOperation("生成代码（下载方式）")
    public void download(
            HttpServletResponse response,
            @ApiParam(name = "tableName", value = "表业务名称", required = true)
            @PathVariable("tableName") String tableName
    ) throws IOException{
        byte[] data = genTableService.downloadCode(tableName);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @Log(title = "代码生成", businessType = BusinessTypeEnum.GENCODE)
    @GetMapping("/genCode/{tableName}")
    @ApiOperation("生成代码（自定义路径）")
    public Response genCode(
            @ApiParam(name = "tableName", value = "表业务名称", required = true)
            @PathVariable("tableName") String tableName
    ){
        genTableService.generatorCode(tableName);
        return Response.success();
    }

    /**
     * 同步数据库
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:edit')")
    @Log(title = "代码生成", businessType = BusinessTypeEnum.UPDATE)
    @GetMapping("/synchDb/{tableName}")
    @ApiOperation("同步数据库")
    public Response synchDb(
            @ApiParam(name = "tableName", value = "表业务名称", required = true)
            @PathVariable("tableName") String tableName
    ){
        genTableService.synchDb(tableName);
        return Response.success();
    }

    /**
     * 批量生成代码
     */
    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @Log(title = "代码生成", businessType = BusinessTypeEnum.GENCODE)
    @GetMapping("/batchGenCode")
    @ApiOperation("批量生成代码")
    public void batchGenCode(
            HttpServletResponse response,
            @ApiParam(name = "tables", value = "表名称tableNames", required = true)
            @RequestParam(name = "tables") String tables
    ) throws IOException{
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.downloadCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException
    {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
