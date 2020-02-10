package com.simbest.boot.wfengine.api;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <strong>Title : ProcessInstVariableApi</strong><br>
 * <strong>Description : 流程参数设置和获取API</strong><br>
 * <strong>Create on : 2019/01/13</strong><br>
 * <strong>Modify on : 2019/01/13</strong><br>
 * <strong>Copyright (C) Ltd.</strong><br>
 *
 * @author LJW lijianwu@simbest.com.cn
 * @version <strong>V1.0.0</strong><br>
 * <strong>修改历史:</strong><br>
 * 修改人 修改日期 修改描述<br>
 * -------------------------------------------<br>
 */
@Slf4j
@Component
public class ProcessInstVariableApi {

    @Autowired
    private BaseFlowableProcessApi baseFlowableProcessApi;

    /**
     * 根据流程实例ID查询流程实例执行流获取流程中设置的参数
     * @param processInstance       流程实例对象
     * @param paramName             获取流程参数的名称
     * @return
     */
    public Object getProcessVariable( ProcessInstance processInstance, String paramName ){
        Execution execution = baseFlowableProcessApi.getRuntimeService().createExecutionQuery().processInstanceId( processInstance.getId() ).singleResult();
        Object paramVlaue = baseFlowableProcessApi.getRuntimeService().getVariable( execution.getId(),paramName );
        return paramVlaue;
    }

    /**
     * 根据流程实例ID查询流程实例执行流设置流程参数
     * @param processInstance       流程实例对象
     * @param params                 要设置的流程参数
     * @return
     */
    public void setProcessVariable( ProcessInstance processInstance, Map<String,Object> params ){
        Execution execution = baseFlowableProcessApi.getRuntimeService().createExecutionQuery().processInstanceId( processInstance.getId() ).singleResult();
        baseFlowableProcessApi.getRuntimeService().setVariables( execution.getId(),params );
    }
}
