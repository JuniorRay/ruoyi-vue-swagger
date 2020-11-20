package com.ruoyi.common.core.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 【Response返回状态的枚举类】
 *
 * @author JuniorRay
 * @date 2020-11-12
 */
public enum ResponseEnum {
    /**********************通用日志信息***************************/

    /**操作成功**/
    SUCCESS(200,"操作成功"),
    /**操作失败**/
    FAIL(000,"操作失败"),
    /**服务端异常**/
    ERROR(500,"系统内部错误"),
    /**对象创建成功**/
    CREATED ( 201,"对象创建成功"),
    /**请求已经被接受**/
    ACCEPTED(202,"请求已经被接受"),
    /**操作已经执行成功，但是没有返回数据**/
    NO_CONTENT(204,"操作已经执行成功，但是没有返回数据"),
    /**资源已被移除**/
    MOVED_PERM(301,"资源已被移除"),
    /**重定向*/
    SEE_OTHER(303,"重定向"),
    /**资源没有被修改**/
    NOT_MODIFIED(304,"资源没有被修改"),
    /**参数列表错误（缺少，格式不匹配**/
    BAD_REQUEST(400,"参数列表错误（缺少，格式不匹配）"),
    /**未授权**/
    UNAUTHORIZED(401,"未授权"),
    /**访问受限，授权过期**/
    FORBIDDEN(403,"访问受限，授权过期"),
    /**资源，服务未找到**/
    NOT_FOUND(404,"资源，服务未找到"),
    /**不允许的http方法**/
    BAD_METHOD(405,"不允许的http方法"),
    /**资源冲突，或者资源被锁**/
    CONFLICT(409,"资源冲突，或者资源被锁"),
    /**不支持的数据，媒体类型**/
    UNSUPPORTED_TYPE(415,"不支持的数据，媒体类型"),
    /**接口未实现**/
    NOT_IMPLEMENTED(501,"接口未实现"),

    /**********************基本增删改查信息***************************/
    /**重复提交**/
    REPEAT_SUBMIT(9999,"不允许重复提交，请稍后再试"),
    ADD_FAIL(9901,"新增失败"),
    ADD_SUCCESS(9902,"新增成功"),
    DELETE_FAIL(9903,"删除失败"),
    DELETE_SUCCESS(9904,"删除成功"),
    UPDATE_FAIL(9905,"修改失败"),
    UPDATE_SUCCESS(9906,"修改成功"),
    QUERY_FAIL(9907,"查询失败"),
    QUERY_SUCCESS(9908,"查询成功"),

    /**********************其他日志信息***************************/
    /**菜单模块**/
    MENU_ADD_ERROR_EXIST(9001,"新增菜单失败，菜单名称已存在"),
    MENU_ADD_ERROR_HTTP(9002,"新增菜单失败，地址必须以http(s)://开头"),
    MENU_ADD_ERROR_SELF(9003,"新增菜单失败，上级菜单不能选择自己"),
    MENU_DELETE_ERROR_SUB(9004,"删除菜单失败，存在子菜单,不允许删除"),
    MENU_DELETE_ERROR_ROLE(9005,"删除菜单失败，菜单已分配角色,不允许删除"),
    MENU_UPDATE_ERROR_EXIST(9006,"修改菜单失败，菜单名称已存在"),
    MENU_UPDATE_ERROR_HTTP(9007,"修改菜单失败，地址必须以http(s)://开头"),
    MENU_UPDATE_ERROR_SELF(9008,"修改菜单失败，上级菜单不能选择自己"),
    /**参数模块**/
    CONFIG_ADD_ERROR_EXIST(9010,"新增参数失败,参数键名已存在"),
    CONFIG_UPDATE_ERROR_EXIST(9011,"修改参数失败,参数键名已存在"),
    /**部门模块**/
    DEPARTMENT_ADD_ERROR_EXIST(9021,"新增部门失败,部门名称已存在"),
    DEPARTMENT_UPDATE_ERROR_EXIST(9022,"修改部门失败,部门名称已存在"),
    DEPARTMENT_UPDATE_ERROR_SELF(9023,"修改部门失败,上级部门不能是自己"),
    DEPARTMENT_UPDATE_ERROR_SUB(9024,"修改部门失败,该部门包含未停用的子部门！"),
    DEPARTMENT_DELETE_ERROR_SUB(9025,"删除部门失败,存在下级部门,不允许删除！"),
    DEPARTMENT_DELETE_ERROR_USER(9026,"删除部门失败,部门存在用户,不允许删除！"),
    /**字典模块**/
    DICTIONARY_ADD_ERROR_EXIST(9030,"新增字典失败,字典类型已存在"),
    DICTIONARY_UPDATE_ERROR_EXIST(9031,"修改字典失败,字典类型已存在"),
    /**岗位模块**/
    POST_ADD_ERROR_EXIST_NAME(9040,"新增岗位失败,岗位名称已存在"),
    POST_ADD_ERROR_EXIST_CODE(9041,"新增岗位失败,岗位编码已存在"),
    POST_UPDATE_ERROR_EXIST_NAME(9042,"修改岗位失败,岗位名称已存在"),
    POST_UPDATE_ERROR_EXIST_CODE(9043,"修改岗位失败,岗位编码已存在"),
    /**角色模块**/
    ROLE_ADD_ERROR_EXIST_NAME(9040,"新增角色失败,角色名称已存在"),
    ROLE_ADD_ERROR_EXIST_AUTHORITY(9041,"新增角色失败,角色权限已存在"),
    ROLE_UPDATE_ERROR_EXIST_NAME(9042,"修改角色失败,角色名称已存在"),
    ROLE_UPDATE_ERROR_EXIST_AUTHORITY(9043,"修改角色失败,角色权限已存在"),
    ROLE_UPDATE_ERROR(9044,"修改角色失败"),
    /**用户模块**/
    USER_ADD_ERROR_USERNAME(9050,"新增用户失败,登录账号已存在"),
    USER_ADD_ERROR_PHONE(9051,"新增用户失败,手机号码已存在"),
    USER_ADD_ERROR_MAIL(9052,"新增用户失败,邮箱账号已存在"),
    USER_UPDATE_ERROR_PHONE(9053,"修改用户失败,手机号码已存在"),
    USER_UPDATE_ERROR_MAIL(9054,"修改用户失败,邮箱账号已存在"),
    USER_UPDATE_ERROR(9055,"修改个人信息异常，请联系管理员"),
    UPDATE_PASSWORD_ERROR_OLD_ERROR(9056,"修改密码失败，旧密码错误"),
    UPDATE_PASSWORD_ERROR_OLD_REPEAT(9057,"新密码和旧密码重复，请重新修改密码"),
    UPDATE_PASSWORD_ERROR(9058,"修改密码异常，请联系管理员"),
    /**登录模块**/
    LOGIN_SUCCESS(9100,"登录成功"),
    LOGIN_FAIL(9101,"登录失败"),
    LOGOUT_SUCCESS(9102,"退出成功"),
    LOGOUT_FAIL(9103,"退出失败"),
    /**其他**/
    DEMO_MODE(9114,"演示模式，不允许操作"),
    CRON_ERROR(9115,"cron表达式不正确"),
    ABNORMAL_PICTURE_UPLOAD(9116,"上传图片异常，请联系管理员");


    // 成员变量
    private Integer code;
    private String message;

    // 构造方法
    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private static final Map<Integer, ResponseEnum> ENUM_MAP = new HashMap<Integer, ResponseEnum>();

    static {
        for (ResponseEnum item : values()) {
            ENUM_MAP.put(item.getCode(), item);
        }
    }

    public Integer getCode() {
        return  this.code ;
    }
    public String getMessage() {
        return  this.message ;
    }

    public static ResponseEnum getModelByKey(Integer code) {
        if (code != null) {
            return ENUM_MAP.get(code);
        }
        return null;
    }

}
