package com.simbest.boot.wfengine.util;

import java.util.UUID;

/**
 * @author Administrator
 * @create 2019/6/12 14:27.
 * 接口常用常量
 */
public class ConstantsUtils {
    //最大重试次数
    public static final Integer MAXEXECUTETIMES = 3;
    //初始
    public static final Integer INITIAL = 0;
    //成功
    public static final Integer SUCCESS = 1;
    //失败
    public static final Integer FAILE = 2;
    //操作成功
    public static final String SUCCESSMES = "操作成功！";
    //操作失败
    public static final String FAILEMES = "操作失败！";
    //未知错误
    public static final String ERRORMES = "接口调用未知错误！";

	public static String getInstanceId(){
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}
}
