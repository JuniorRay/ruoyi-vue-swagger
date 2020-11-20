package com.ruoyi.common.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【Response返回实体类】
 *
 * @author JuniorRay
 * @date 2020-11-12
 */
@ApiModel(description = "结果返回类")
public class Response<T> {
    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "返回内容")
    private String msg;

    @ApiModelProperty(value = "数据对象")
    private T data;

    /**
     * 构造方法私有化，不允许外部new Response
     * @param data
     */
    private Response(T data){
        this.code = ResponseEnum.SUCCESS.getCode();
        this.msg = ResponseEnum.SUCCESS.getMessage();
        this.data = data;
    }

    /**
     * 构造方法私有化，不允许外部new Response
     * @param responseEnum
     */
    private Response(ResponseEnum responseEnum){
        if (null == responseEnum) {
            return;
        }
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getMessage();
    }

    /**
     * 成功时调用
     * @param
     * @param <T>
     * @return
     */
    public static <T> Response<T> success(){
        return success(null);
    }
    /**
     * 成功时调用
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Response<T> success(T data){
        return new Response<T>(data);
    }
    /**
     * 失败时调用
     * @param
     * @param <T>
     * @return
     */
    public static <T> Response error(){
        return error(ResponseEnum.ERROR);
    }
    /**
     * 失败时调用
     * @param responseEnum
     * @param <T>
     * @return
     */
    public static <T> Response error(ResponseEnum responseEnum){
        return new Response<T>(responseEnum);
    }



    public Integer getCode() {
        return code;
    }

    public Response<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Response<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Response<T> setData(T data) {
        this.data = data;
        return this;
    }
}
