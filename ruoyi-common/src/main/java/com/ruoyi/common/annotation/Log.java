package com.ruoyi.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.ruoyi.common.enums.BusinessTypeEnum;
import com.ruoyi.common.enums.OperatorTypeEnum;

/**
 * 自定义操作日志记录注解
 *
 * @author ruoyi
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    /**
     * 模块
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessTypeEnum businessType() default BusinessTypeEnum.OTHER;

    /**
     * 操作人类别
     */
    public OperatorTypeEnum operatorType() default OperatorTypeEnum.MANAGE;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;
}
