package com.simbest.boot.wfengine.util;

/**
 * 常用URL
 * flowable全局监听类把流程数据返回给客户端
 */
public class ConstansURL {

    public static final String PROCESS_INSTANCE_CREATED = "/action/flowable/processlistener/anonymous/start";	//实例创建
    public static final String PROCESS_INSTANCE_ENDED = "/action/flowable/processlistener/anonymous/completed";	//	实例完成
    public static final String TASK_CREATED = "/action/flowable/tasklistener/anonymous/created";	//	任务创建
    public static final String TASK_COMPLETED = "/action/flowable/tasklistener/anonymous/completed";	//	任务完成

}
