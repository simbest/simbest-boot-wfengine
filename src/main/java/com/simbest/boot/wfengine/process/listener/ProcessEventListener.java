package com.simbest.boot.wfengine.process.listener;//package com.simbest.boot.wfdriver.process.listener;

import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Maps;
import com.simbest.boot.util.DateUtil;
import com.simbest.boot.util.json.JacksonUtils;
import com.simbest.boot.util.security.LoginUtils;
import com.simbest.boot.wfengine.api.BaseFlowableProcessApi;
import com.simbest.boot.wfengine.process.api.CallFlowableProcessApi;
import com.simbest.boot.wfengine.rabbitmq.model.MqSend;
import com.simbest.boot.wfengine.rabbitmq.service.IMqSendService;
import com.simbest.boot.wfengine.util.ConstansAction;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 *@ClassName ProcessEventListener
 *@Description 全局监听配置类，监听实例创建和实例完成
 *@Author Administrator
 *@Date 2019/12/9 11:59
 *@Version 1.0
 **/
@Slf4j
@Component
public class ProcessEventListener implements FlowableEventListener {

    @Autowired
    private BaseFlowableProcessApi baseFlowableProcessApi;

    @Autowired
    private CallFlowableProcessApi callFlowableProcessApi;

    @Autowired
    private IMqSendService mqSendServiceImpl;
    @Autowired
    private LoginUtils loginUtils;

    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        loginUtils.adminLogin();
        log.debug("--------------------------------ProcessEventListener监听开始-----------------");
        FlowableEngineEventType flowableEventType = (FlowableEngineEventType) flowableEvent.getType();
        FlowableEntityEvent flowableEntityEvent =(FlowableEntityEvent) flowableEvent;
        HistoricProcessInstanceEntity historyInstance = (HistoricProcessInstanceEntity) flowableEntityEvent.getEntity();
        // 流程启动后获取变量
        //Map<String,Object> variables = baseFlowableProcessApi.getRuntimeService().getVariables(historyInstance.getProcessInstanceId());
        Map<String,Object> variables = historyInstance.getProcessVariables();
        //log.debug( "获取流程中的流程实例实体【{}】", historyInstance.toString() );
        log.debug( "获取流程中的参数【{}】", JacksonUtils.obj2json( variables ) );
        String orgCode = MapUtil.getStr( variables,"orgCode" );
        String postId = MapUtil.getStr( variables,"postId" );
        String inputUserId = MapUtil.getStr( variables,"inputUserId" );
        Map<String ,Object> map=Maps.newHashMap();
        String tenantId = historyInstance.getTenantId();
        map.put("tenantId",historyInstance.getTenantId());
        map.put("processInstanceId",historyInstance.getProcessInstanceId());
        map.put("name",historyInstance.getName());
        map.put("description",historyInstance.getDescription());
        map.put("businessKey",historyInstance.getBusinessKey());
        map.put("callbackId",historyInstance.getCallbackId());
        map.put("callbackType",historyInstance.getCallbackType());
        map.put("deleteReason",historyInstance.getDeleteReason());
        map.put("deploymentId",historyInstance.getDeploymentId());
        map.put("durationInMillis",historyInstance.getDurationInMillis());
        map.put("endActivityId",historyInstance.getEndActivityId());
        map.put("endTime",historyInstance.getEndTime()!=null?DateUtil.getDate(historyInstance.getEndTime(),DateUtil.timestampPattern1):null);
        map.put("processDefinitionId",historyInstance.getProcessDefinitionId());
        map.put("processDefinitionKey",historyInstance.getProcessDefinitionKey());
        map.put("processDefinitionName",historyInstance.getProcessDefinitionName());
        map.put("processDefinitionVersion",historyInstance.getProcessDefinitionVersion());
        map.put("revision",historyInstance.getRevision());
        map.put("startActivityId",historyInstance.getStartActivityId());
        map.put("startTime",historyInstance.getStartTime()!=null?DateUtil.getDate(historyInstance.getStartTime(),DateUtil.timestampPattern1):null);
        map.put("startUserId",historyInstance.getStartUserId());
        map.put("superProcessInstanceId",historyInstance.getSuperProcessInstanceId());
//        map.put( "creatorIdentity",historyInstance.getStartUserId().concat( "#" ).concat( orgCode ).concat( "#" ).concat( postId ) );

        switch (flowableEventType) {
            case HISTORIC_PROCESS_INSTANCE_CREATED:
                log.debug("--------------------------------HISTORIC_PROCESS_INSTANCE_CREATED监听开始-----------------");
                if(historyInstance.getName()==null){
                    callFlowableProcessApi.process_instance_created(tenantId,map);
                }else if(historyInstance.getName()!=null && "rabbitmq".equals(historyInstance.getName())){
                    MqSend mqSend = new MqSend();
                    mqSend.setEnabled(true);
                    mqSend.setCreatedTime(DateUtil.date2LocalDateTime(new Date()));
                    mqSend.setBusinessKey(historyInstance.getBusinessKey());
                    mqSend.setProcessDefKey(historyInstance.getProcessDefinitionKey());
                    mqSend.setProcessInstId(historyInstance.getProcessInstanceId());
                    mqSend.setAction(ConstansAction.INSTANCESSTARTBYKEY);
                    mqSend.setTenantId(tenantId);
                    mqSend.setIsSend(0);
                    mqSend.setIsSuccess(-1);//不需要回复成功
                    mqSend.setMapJson(JacksonUtils.obj2json(map));
                    //保存消息
                    mqSendServiceImpl.insert(mqSend);
                    //发送消息到rabbitmq
                    mqSendServiceImpl.sendSms(mqSend);
                }
                break;
            case HISTORIC_PROCESS_INSTANCE_ENDED:
                log.debug("--------------------------------HISTORIC_PROCESS_INSTANCE_ENDED监听开始-----------------");
                if(historyInstance.getName()==null){
                    callFlowableProcessApi.process_instance_ended(tenantId,map);
                }else if(historyInstance.getName()!=null && "rabbitmq".equals(historyInstance.getName())){

                }
                break;
            default:
        }
        log.debug("--------------------------------ProcessEventListener监听结束-----------------");
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }


//    /**
//     * 监听流程事件
//     * @param delegateExecution     流程监听对象
//     */
//    @Override
//    public void notify ( DelegateExecution delegateExecution ) {
//        String activitiEventType = delegateExecution.getEventName();
//        switch (activitiEventType) {
//            case EVENTNAME_START:
//                log.debug("流程启动");
//                break;
//            case EVENTNAME_END:
//                log.debug("流程完成");
//                break;
//            case EVENTNAME_TAKE:   //take是监控连线的5时候使用的
//                log.debug("监控连线");
//                break;
//        }
//    }

//    /**
//     * 监听流程事件
//     * @param activitiEvent     流程监听对象
//     *//*
//    @Override
//    public void onEvent ( ActivitiEvent activitiEvent ) {
//        ActivitiEventType activitiEventType = activitiEvent.getType();
//        if(ActivitiEventType.PROCESS_STARTED.equals(activitiEventType)){
//            log.debug("流程启动");
//        }
//        if(ActivitiEventType.PROCESS_COMPLETED.equals(activitiEventType)){
//            log.debug("流程完成");
//        }
//    }



}
