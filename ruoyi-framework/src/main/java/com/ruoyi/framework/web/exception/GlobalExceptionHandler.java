package com.ruoyi.framework.web.exception;

import com.ruoyi.common.core.domain.Response;
import com.ruoyi.common.core.domain.ResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.ruoyi.common.exception.BaseException;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.exception.DemoModeException;
import com.ruoyi.common.utils.StringUtils;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 基础异常
     */
    @ExceptionHandler(BaseException.class)
    public Response baseException(BaseException e)
    {
        return Response.error().setMsg(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public Response businessException(CustomException e)
    {
        if (StringUtils.isNull(e.getCode()))
        {
            return Response.error().setMsg((e.getMessage()));
        }
        return Response.error().setCode(e.getCode()).setMsg(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Response handlerNoFoundException(Exception e)
    {
        log.error(e.getMessage(), e);
        return Response.error(ResponseEnum.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Response handleAuthorizationException(AccessDeniedException e)
    {
        log.error(e.getMessage());
        return Response.error(ResponseEnum.FORBIDDEN);
    }

    @ExceptionHandler(AccountExpiredException.class)
    public Response handleAccountExpiredException(AccountExpiredException e)
    {
        log.error(e.getMessage(), e);
        return Response.error().setMsg(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public Response handleUsernameNotFoundException(UsernameNotFoundException e)
    {
        log.error(e.getMessage(), e);
        return Response.error().setMsg(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e)
    {
        log.error(e.getMessage(), e);
        return Response.error().setMsg(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public Response validatedBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return Response.error().setMsg(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Response.error().setMsg(message);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public Response demoModeException(DemoModeException e)
    {
        return Response.error(ResponseEnum.DEMO_MODE);
    }
}
