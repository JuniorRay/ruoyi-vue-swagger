package com.ruoyi.system.controller.monitor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Response;
import com.ruoyi.framework.web.domain.Server;

/**
 * 服务器监控
 * 添加Response swagger识别
 * @author JuniorRay
 */
@RestController
@RequestMapping("/monitor/server")
@Api(tags={"【服务器监控】Controller"})
public class ServerController extends BaseController
{
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    @ApiOperation("获取服务器监控信息")
    public Response<Server> getInfo() throws Exception
    {
        Server server = new Server();
        server.copyTo();
        return Response.success(server);
    }
}
