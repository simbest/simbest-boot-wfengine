package com.simbest.boot.wfengine.provide.history.service;

import org.flowable.engine.history.HistoricProcessInstance;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @create 2019/12/4 11:21.
 */
public interface IProcessHistoryService {
    HistoricProcessInstance historyInstancesGet(String processInstanceId);

    List<HistoricProcessInstance> historyInstancesQuery(Map<String,Object> map);
}
