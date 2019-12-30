package com.simbest.boot.wfengine.util;

/**
 * @author Administrator
 * @create 2019/6/4 10:27.
 * 接口常用常量
 */
public class JsonResponse{
    public static final int SUCCESS_CODE = 0;
    public static final int ERROR_CODE = -1;
    public static final String SUCCESS_MES = "同步成功";
    public static final String ERROR_MES = "同步失败";
    public static final String CODE_NULL = "来源系统Code不存在";
    public static final String TOKEN_ERROR = "TOKEN校验失败，请检查";
    public static final String TOKEN_VALIDITY = "TOKEN已失效，请检查";
    public static final String IP_ERROR = "来访IP不在系统白名单中，请检查";

    private Integer errcode;
    private String message;
    private Object data;
    private String instanceId;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}