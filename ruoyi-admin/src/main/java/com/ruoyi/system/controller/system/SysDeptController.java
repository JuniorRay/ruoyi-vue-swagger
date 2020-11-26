package com.ruoyi.system.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.domain.ResponseEnum;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.dto.system.RoleDeptDTO;
import com.ruoyi.system.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/**
 * 部门信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dept")
@Api(tags={"【部门信息】Controller"})
public class SysDeptController extends BaseController
{
    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    @ApiOperation("查询部门信息列表")
    public Response<List<SysDept>> list(@ModelAttribute SysDept dept)
    {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return Response.success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    @ApiOperation("查询部门信息列表（排除节点）")
    public Response<List<SysDept>> excludeChild(
            @ApiParam(name = "deptId", value = "部门ID", required = true)
            @PathVariable(value = "deptId", required = false) Long deptId
    ){
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext())
        {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""))
            {
                it.remove();
            }
        }
        return Response.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    @ApiOperation("根据部门编号获取详细信息")
    public Response<SysDept> getInfo(
            @ApiParam(name = "deptId", value = "部门ID", required = true)
            @PathVariable("deptId") Long deptId
    ){
        return Response.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    @ApiOperation("获取部门下拉树列表")
    public Response<List<TreeSelect>> treeselect(@ModelAttribute SysDept dept)
    {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return Response.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    @ApiOperation("加载对应角色部门列表树")
    public Response<RoleDeptDTO> roleDeptTreeselect(
            @ApiParam(name = "roleId", value = "角色ID", required = true)
            @PathVariable("roleId") Long roleId
    ){
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        RoleDeptDTO roleDeptDTO = new RoleDeptDTO();
        roleDeptDTO.setCheckedKeys(deptService.selectDeptListByRoleId(roleId));
        roleDeptDTO.setDepts(deptService.buildDeptTreeSelect(depts));
        return Response.success(roleDeptDTO);
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
    @ApiOperation("新增部门")
    public Response<Integer> add(@Validated @RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return Response.error(ResponseEnum.DEPARTMENT_ADD_ERROR_EXIST);
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        return toResponse(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改部门")
    public Response<Integer> edit(@Validated @RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return Response.error(ResponseEnum.DEPARTMENT_UPDATE_ERROR_EXIST);
        }
        else if (dept.getParentId().equals(dept.getDeptId()))
        {
            return Response.error(ResponseEnum.DEPARTMENT_UPDATE_ERROR_SELF);
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0)
        {
            return Response.error(ResponseEnum.DEPARTMENT_UPDATE_ERROR_SUB);
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{deptId}")
    @ApiOperation("删除部门")
    public Response<Integer> remove(
            @ApiParam(name = "deptId", value = "部门id", required = true)
            @PathVariable("deptId") Long deptId
    ){
        if (deptService.hasChildByDeptId(deptId))
        {
            return Response.error(ResponseEnum.DEPARTMENT_DELETE_ERROR_SUB);
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return Response.error(ResponseEnum.DEPARTMENT_DELETE_ERROR_USER);
        }
        return toResponse(deptService.deleteDeptById(deptId));
    }
}
