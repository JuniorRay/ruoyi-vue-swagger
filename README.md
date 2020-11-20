## RuoYi-Vue-Swagger平台简介

* 基于RuoYi-Vue基础上进行开发，原项目地址：https://gitee.com/y_project/RuoYi-Vue
* 集成swagger-bootstrap-ui 打开地址：http://localhost:8080/doc.html
* 改造所有返回值AjaxResult为Response让Swagger的@ApiModel识别（因为swagger不支持返回的hashMap和Json形式）
* 改造系统返回DTO层，规范项目开发
* 改造axios和controller交互，规范项目restful
* 修改全局domain.vm模板和controller.vm模板，一键生成带swagger的增删改查全套代码
* Response<T>直接返回结果和ResponsePageInfo<T>泛型分页代码生成更快速识别swagger
* ResponseEnum统一状态消息管理，方便排查问题
* 新增create_db.sql脚本，方便一键生成数据库和用户
* create_business.用于存放你们自己业务的sql，quartz.sql是定时任务sql，ry_20201021.sql是系统基本功能自带的sql
* 新增登录方法（不含验证码） /login/noCode ,方便生成token用swagger登录Authorize授权测试
* 前端采用Vue、Element UI。
* 后端采用Spring Boot、Spring Security、Redis & Jwt、Swagger2 、 Swagger-Bootstrap-UI、 Lombok。
* 权限认证使用Jwt，支持多终端认证系统。
* 支持加载动态权限菜单，多方式轻松权限控制。
* 高效率开发，使用代码生成器可以一键生成前后端代码。

## 内置功能

1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
14. 系统接口：根据业务代码自动生成相关的api接口文档。
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
16. 在线构建器：拖动表单元素生成相应的HTML代码。
17. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。

## 模板生成代码示范（局部）

```java
package com.ruoyi.project.controller;

import java.util.List;
import io.swagger.annotations.*;
import com.ruoyi.common.core.domain.Response;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.project.domain.OmExamOrder;
import com.ruoyi.project.service.IOmExamOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.ResponsePageInfo;

/**
 * 【检查申请单】Controller
 *
 * @author JuniorRay
 * @date 2020-11-20
 */
@RestController
@RequestMapping("/checkApplicationForm/examOrder")
@Api(tags={"【检查申请单】Controller"})
public class OmExamOrderController extends BaseController{
    @Autowired
    private IOmExamOrderService omExamOrderService;

    /**
     * 查询检查申请单列表
     */
    @PreAuthorize("@ss.hasPermi('checkApplicationForm:examOrder:list')")
    @GetMapping("/list")
    @ApiOperation("查询检查申请单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码" , paramType = "query", required = false),
            @ApiImplicitParam(name = "pageSize",value = "每页数据量" , paramType = "query", required = false),

    })
    public ResponsePageInfo<OmExamOrder> list(@ModelAttribute OmExamOrder omExamOrder){
        startPage();
        List<OmExamOrder> list = omExamOrderService.selectOmExamOrderList(omExamOrder);
        return toResponsePageInfo(list);
    }

    /**
     * 导出检查申请单列表
     */
    @PreAuthorize("@ss.hasPermi('checkApplicationForm:examOrder:export')")
    @Log(title = "检查申请单", businessType = BusinessTypeEnum.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出检查申请单列表Excel")
    public Response<String> export(@ModelAttribute OmExamOrder omExamOrder){
        List<OmExamOrder> list = omExamOrderService.selectOmExamOrderList(omExamOrder);
        ExcelUtil<OmExamOrder> util = new ExcelUtil<OmExamOrder>(OmExamOrder.class);
        return util.exportExcel(list, "examOrder");
    }

    /**
     * 获取检查申请单详细信息
     */
    @PreAuthorize("@ss.hasPermi('checkApplicationForm:examOrder:query')")
    @GetMapping(value = "/{keyId}")
    @ApiOperation("获取检查申请单详细信息")
    public Response<OmExamOrder> getInfo(
            @ApiParam(name = "keyId", value = "检查申请单参数", required = true)
            @PathVariable("keyId") String keyId
    ){
        return Response.success(omExamOrderService.selectOmExamOrderById(keyId));
    }

    /**
     * 新增检查申请单
     */
    @PreAuthorize("@ss.hasPermi('checkApplicationForm:examOrder:add')")
    @Log(title = "检查申请单", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
    @ApiOperation("新增检查申请单")
    public Response<Integer> add(@RequestBody OmExamOrder omExamOrder){
        return toResponse(omExamOrderService.insertOmExamOrder(omExamOrder));
    }

    /**
     * 修改检查申请单
     */
    @PreAuthorize("@ss.hasPermi('checkApplicationForm:examOrder:edit')")
    @Log(title = "检查申请单", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改检查申请单")
    public Response<Integer> edit(@RequestBody OmExamOrder omExamOrder){
        return toResponse(omExamOrderService.updateOmExamOrder(omExamOrder));
    }

    /**
     * 删除检查申请单
     */
    @PreAuthorize("@ss.hasPermi('checkApplicationForm:examOrder:remove')")
    @Log(title = "检查申请单", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{keyIds}")
    @ApiOperation("删除检查申请单")
    public Response remove(
            @ApiParam(name = "keyIds", value = "检查申请单ids参数", required = true)
            @PathVariable String[] keyIds
    ){
        return toResponse(omExamOrderService.deleteOmExamOrderByIds(keyIds));
    }
}

````

## 在线体验

- admin/admin123  

演示地址：http://vue.ruoyi.vip  
文档地址：http://doc.ruoyi.vip

## 演示图
![Image text](doc/doc-show.png)

<table>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/cd1f90be5f2684f4560c9519c0f2a232ee8.jpg"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/1cbcf0e6f257c7d3a063c0e3f2ff989e4b3.jpg"/></td>
    </tr>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-8074972883b5ba0622e13246738ebba237a.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-9f88719cdfca9af2e58b352a20e23d43b12.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-39bf2584ec3a529b0d5a3b70d15c9b37646.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-936ec82d1f4872e1bc980927654b6007307.png"/></td>
    </tr>
	<tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-b2d62ceb95d2dd9b3fbe157bb70d26001e9.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-d67451d308b7a79ad6819723396f7c3d77a.png"/></td>
    </tr>	 
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/5e8c387724954459291aafd5eb52b456f53.jpg"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/644e78da53c2e92a95dfda4f76e6d117c4b.jpg"/></td>
    </tr>
	<tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-8370a0d02977eebf6dbf854c8450293c937.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-49003ed83f60f633e7153609a53a2b644f7.png"/></td>
    </tr>
	<tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-d4fe726319ece268d4746602c39cffc0621.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-c195234bbcd30be6927f037a6755e6ab69c.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/b6115bc8c31de52951982e509930b20684a.jpg"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-6d73c2140ce694e3de4c05035fdc1868d4c.png"/></td>
    </tr>
</table>

## Swagger使用：

1.未登录授权前，接口无法调试

![Image text](doc/login-before.png)

2.登录用法-用登录（免验证码）

![Image text](doc/login.png)

3.然后保存

![Image text](doc/login-save.png)

4.之后就能愉快的调试了

![Image text](doc/login-after.png)
