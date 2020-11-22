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
import com.ruoyi.common.core.page.ResponsePageInfo;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.bean.SysPost;
import com.ruoyi.system.service.ISysPostService;

/**
 * 岗位信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/post")
@Api(tags={"【岗位信息操作处理】Controller"})
public class SysPostController extends BaseController
{
    @Autowired
    private ISysPostService postService;

    /**
     * 获取岗位列表
     */
    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @GetMapping("/list")
    @ApiOperation("获取岗位列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" , paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , paramType = "query", required = false),

    })
    public ResponsePageInfo<SysPost> list(@ModelAttribute SysPost post)
    {
        startPage();
        List<SysPost> list = postService.selectPostList(post);
        return toResponsePageInfo(list);
    }

    @Log(title = "岗位管理", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:post:export')")
    @GetMapping("/export")
    @ApiOperation("导出岗位列表Excel")
    public Response<String> export(@ModelAttribute SysPost post)
    {
        List<SysPost> list = postService.selectPostList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        return util.exportExcel(list, "岗位数据");
    }

    /**
     * 根据岗位编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @GetMapping(value = "/{postId}")
    @ApiOperation("根据岗位编号获取详细信息")
    public Response<SysPost> getInfo(
            @ApiParam(name = "postId", value = "岗位编号", required = true)
            @PathVariable("postId") Long postId
    ){
        return Response.success(postService.selectPostById(postId));
    }

    /**
     * 新增岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:add')")
    @Log(title = "岗位管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
    @ApiOperation("新增岗位")
    public Response<Integer> add(@Validated @RequestBody SysPost post)
    {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post)))
        {
            return Response.error(ResponseEnum.POST_ADD_ERROR_EXIST_NAME);
        }
        else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post)))
        {
            return Response.error(ResponseEnum.POST_ADD_ERROR_EXIST_CODE);
        }
        post.setCreateBy(SecurityUtils.getUsername());
        return toResponse(postService.insertPost(post));
    }

    /**
     * 修改岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @Log(title = "岗位管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改岗位")
    public Response<Integer> edit(@Validated @RequestBody SysPost post)
    {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post)))
        {
            return Response.error(ResponseEnum.POST_UPDATE_ERROR_EXIST_NAME);
        }
        else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post)))
        {
            return Response.error(ResponseEnum.POST_UPDATE_ERROR_EXIST_CODE);
        }
        post.setUpdateBy(SecurityUtils.getUsername());
        return toResponse(postService.updatePost(post));
    }

    /**
     * 删除岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:remove')")
    @Log(title = "岗位管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{postIds}")
    @ApiOperation("删除岗位")
    public Response<Integer> remove(
            @ApiParam(name = "postIds", value = "岗位编号ids{逗号分隔}", required = true)
            @PathVariable Long[] postIds
    ){
        return toResponse(postService.deletePostByIds(postIds));
    }

    /**
     * 获取岗位选择框列表
     */
    @GetMapping("/optionselect")
    @ApiOperation("获取岗位选择框列表")
    public Response<List<SysPost>> optionselect()
    {
        List<SysPost> posts = postService.selectPostAll();
        return Response.success(posts);
    }
}
