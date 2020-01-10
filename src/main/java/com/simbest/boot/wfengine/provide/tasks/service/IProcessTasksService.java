package com.simbest.boot.wfengine.provide.tasks.service;

import com.simbest.boot.wfengine.provide.tasks.model.ProcessTasksInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @create 2019/12/3 21:58.
 */
public interface IProcessTasksService {
    int tasksComplete(String taskId, String stringJson);

    List<ProcessTasksInfo> tasksQuery(Map<String,Object> map);

    ProcessTasksInfo tasksGet(String taskId);

    int tasksAddComment(String currentUserCode,String taskId, String processInstanceId, String comment);
}
