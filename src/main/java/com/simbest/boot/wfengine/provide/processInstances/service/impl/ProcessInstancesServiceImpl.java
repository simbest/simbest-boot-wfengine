package com.simbest.boot.wfengine.provide.processInstances.service.impl;/**
 * @author Administrator
 * @create 2019/12/3 19:31.
 */

import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Maps;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.util.json.JacksonUtils;
import com.simbest.boot.wfengine.api.BaseFlowableProcessApi;
import com.simbest.boot.wfengine.provide.processInstances.service.IProcessInstancesService;
import com.simbest.boot.wfengine.rabbitmq.model.MqReceive;
import com.simbest.boot.wfengine.util.MapRemoveNullUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@ClassName ProcessInstancesServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/3 19:31
 *@Version 1.0
 **/
@Slf4j
@Service
public class ProcessInstancesServiceImpl implements IProcessInstancesService {

    @Autowired
    private BaseFlowableProcessApi baseFlowableProcessApi;
    /**
     * 启动流程实例
     *
     * @param processDefinitionId
     * @param processDefinitionKey
     * @param message
     * @param stringJson
     * @return
     */
    @Override
    public Map<String,Object> instancesStart(String processDefinitionId, String processDefinitionKey, String message, String tenantId,String stringJson) {

        Map<String,Object> result = new HashMap();
        try {
            String processInstanceId = null;
            Map<String,Object> var = JacksonUtils.json2obj(stringJson,HashedMap.class);
            String inputUserId = MapUtil.getStr( var,"inputUserId" );
            String businessKey = MapUtil.getStr( var,"businessKey" );
            //设置流程发起人
            Authentication.setAuthenticatedUserId(inputUserId);
            if(processDefinitionId!=null){
                ProcessInstance pi =baseFlowableProcessApi.getRuntimeService().startProcessInstanceById(processDefinitionId,var);
                processInstanceId = pi.getId();
            }else if(processDefinitionKey!=null){
                ProcessInstance pi = baseFlowableProcessApi.getRuntimeService().startProcessInstanceByKeyAndTenantId(processDefinitionKey,businessKey,var,tenantId);
                processInstanceId = pi.getId();
            }else if(message!=null){
                ProcessInstance pi = baseFlowableProcessApi.getRuntimeService().startProcessInstanceByMessageAndTenantId(message,var,tenantId);
                processInstanceId = pi.getId();
            }
            //这个方法最终使用一个ThreadLocal类型的变量进行存储，也就是与当前的线程绑定，所以流程实例启动完毕之后，需要设置为null，防止多线程的时候出问题。
            Authentication.setAuthenticatedUserId(null);
            result.put("processInstanceId",processInstanceId);
        }catch (Exception e ){
            log.error(Exceptions.getStackTraceAsString(e));
        }
        return result;
    }


    @Override
    public List<ProcessInstance> instancesQuery(Map<String, Object> map) {
        String processDefinitionKey = (String) map.get("processDefinitionKey");
        String processDefinitionId = (String) map.get("processDefinitionId");
        String businessKey = (String) map.get("businessKey");
        String tenantId = (String) map.get("tenantId");
        String processInstanceId = (String) map.get("processInstanceId");
        int page = (int) map.get("page");
        int size = (int) map.get("size");
        List<ProcessInstance> list = baseFlowableProcessApi.getRuntimeService().createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .processDefinitionKey(processDefinitionKey)
                .processDefinitionId(processDefinitionId)
                .processInstanceBusinessKey(businessKey)
                .processInstanceTenantId(tenantId)
                .orderByStartTime()
                .desc()
                .listPage(page,size);
        return list;
    }

    @Override
    public ProcessInstance instancesGet(String processInstanceId) {
        return baseFlowableProcessApi.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    @Override
    public void instancesStart4Mq(MqReceive mqReceive) {

        String mapJson = mqReceive.getMapJson();
        Map<String,Object> startParam  = JacksonUtils.json2obj(mapJson,Map.class);

        String startProcessFlag = MapUtil.getStr( startParam,"startProcessFlag" );
        String currentUserCode = MapUtil.getStr( startParam, "currentUserCode" );
        String orgCode = MapUtil.getStr(startParam, "orgCode" );
        String postId = MapUtil.getStr(startParam, "postId" );
        String message = MapUtil.getStr( startParam, "message" );
        String idValue = MapUtil.getStr( startParam, "idValue" );  //流程的定义ID
        String nextUser = MapUtil.getStr( startParam, "nextUser" );
        String nextUserOrgCode = MapUtil.getStr( startParam, "nextUserOrgCode" );
        String nextUserPostId =  MapUtil.getStr( startParam, "nextUserPostId" );
        String outcome = MapUtil.getStr( startParam, "outcome");
        String businessKey = MapUtil.getStr( startParam, "businessKey" );
        String messageNameValue = MapUtil.getStr( startParam, "messageNameValue" );

        /**
         * 启动流程
         */
        ProcessInstance pi = null;
        Map<String, Object> variables = Maps.newConcurrentMap();
        if ( !StringUtils.isEmpty( startProcessFlag ) ){
            switch ( startProcessFlag ){
                case "KEY":
                    variables.put( "inputUserId", currentUserCode);
                    variables.put( "businessKey", businessKey);
                    variables.put( "orgCode", orgCode);
                    variables.put( "postId", postId);
                    variables.put("rabbitmq","rabbitmq");//字段很关键，决定了该流程实体通过什么方式和应用进行数据交互
                    MapRemoveNullUtil.removeNullEntry(variables);
//                    pi = baseFlowableProcessApi.getRuntimeService().startProcessInstanceByKeyAndTenantId(idValue,businessKey,variables,mqReceive.getTenantId());

                    // 获取流程构造器
                    ProcessInstanceBuilder processInstanceBuilder = baseFlowableProcessApi.getRuntimeService().createProcessInstanceBuilder();
                    processInstanceBuilder.businessKey(businessKey);
                    processInstanceBuilder.tenantId(mqReceive.getTenantId());
                    processInstanceBuilder.processDefinitionKey(idValue);
                    processInstanceBuilder.variables(variables);
                    /*字段很关键，决定了该流程实体通过什么方式和应用进行数据交互*/
                    processInstanceBuilder.name("rabbitmq");
                    pi =  processInstanceBuilder.start();

                    break;
                case "MESSAGE":
                    variables.put( "inputUserId", nextUser);
                    variables.put( "businessKey", businessKey);
                    variables.put("rabbitmq","rabbitmq");//字段很关键，决定了该流程实体通过什么方式和应用进行数据交互
                    MapRemoveNullUtil.removeNullEntry(variables);
//                    pi = baseFlowableProcessApi.getRuntimeService().startProcessInstanceByMessageAndTenantId(messageNameValue,variables,mqReceive.getTenantId());

                    // 获取流程构造器
                    ProcessInstanceBuilder processInstanceBuilderMessage = baseFlowableProcessApi.getRuntimeService().createProcessInstanceBuilder();
                    processInstanceBuilderMessage.businessKey(businessKey);
                    processInstanceBuilderMessage.tenantId(mqReceive.getTenantId());
                    processInstanceBuilderMessage.variables(variables);
                    processInstanceBuilderMessage.messageName(messageNameValue);
                    /*字段很关键，决定了该流程实体通过什么方式和应用进行数据交互*/
                    processInstanceBuilderMessage.name("rabbitmq");
                    pi =  processInstanceBuilderMessage.start();

                    break;
                default:
                    break;
            }
        }
        /**
         * 完成第一个节点
         */
        Task task = baseFlowableProcessApi.getTaskService().createTaskQuery()
                .taskAssignee(currentUserCode)
                .processInstanceId(pi.getProcessInstanceId())
                .singleResult();
        Authentication.setAuthenticatedUserId(currentUserCode);
        baseFlowableProcessApi.getTaskService().addComment(task.getId(), pi.getProcessInstanceId(), message);
        Map<String, Object> nextVar = new HashMap<String,Object>();
        nextVar.put("inputUserId", nextUser);//指定下一审批人
        nextVar.put("outcome", outcome);//指定下一审批环节
        nextVar.put("message", message);//指定下一审批环节
        baseFlowableProcessApi.getTaskService().complete(task.getId(),nextVar);
    }

    /**
     * 删除流程实例
     * @param processInstanceId
     */
    @Override
    public void deleteProcessInstance(String processInstanceId) {
        ProcessInstance pi = baseFlowableProcessApi.getRuntimeService().createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if(pi==null){
        //该流程实例已经完成了
            baseFlowableProcessApi.getHistoryService().deleteHistoricProcessInstance(processInstanceId);
        }else{
            //该流程实例未结束的
            baseFlowableProcessApi.getRuntimeService().deleteProcessInstance(processInstanceId,"");
            baseFlowableProcessApi.getHistoryService().deleteHistoricProcessInstance(processInstanceId);//(顺序不能换)
        }
    }
}
