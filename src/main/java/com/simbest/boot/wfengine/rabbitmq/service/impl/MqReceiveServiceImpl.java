package com.simbest.boot.wfengine.rabbitmq.service.impl;/**
 * @author Administrator
 * @create 2020/2/19 10:31.
 */

import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.util.DateUtil;
import com.simbest.boot.util.json.JacksonUtils;
import com.simbest.boot.wfengine.provide.processInstances.service.IProcessInstancesService;
import com.simbest.boot.wfengine.provide.tasks.service.IProcessTasksService;
import com.simbest.boot.wfengine.rabbitmq.model.MqReceive;
import com.simbest.boot.wfengine.rabbitmq.model.MqSend;
import com.simbest.boot.wfengine.rabbitmq.repository.MqReceiveRepository;
import com.simbest.boot.wfengine.rabbitmq.service.IMqReceiveService;
import com.simbest.boot.wfengine.util.ConstansAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.simbest.boot.config.MultiThreadConfiguration.MULTI_THREAD_BEAN;

/**
 *@ClassName MqReceiveServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2020/2/19 10:31
 *@Version 1.0
 **/
@Slf4j
@Service(value = "mqReceiveServiceImpl")
public class MqReceiveServiceImpl extends LogicService<MqReceive,String> implements IMqReceiveService {

    private MqReceiveRepository repository;

    @Autowired
    private IProcessInstancesService processInstancesService;
    @Autowired
    private IProcessTasksService processTasksServiceImpl;

    @Autowired
    public MqReceiveServiceImpl(MqReceiveRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public boolean handler(String message) {
        if(StringUtils.isNotEmpty(message)){
            MqSend mqSend = JacksonUtils.json2obj(message,MqSend.class);
            MqReceive mqReceive = new MqReceive();

            mqReceive.setCreator("hadmin");
            mqReceive.setModifier("hadmin");
            mqReceive.setEnabled(true);
            mqReceive.setCreatedTime(DateUtil.date2LocalDateTime(new Date()));

            mqReceive.setAction(mqSend.getAction());
            mqReceive.setTenantId(mqSend.getTenantId());
            mqReceive.setBusinessKey(mqSend.getBusinessKey());
            mqReceive.setProcessDefKey(mqSend.getProcessDefKey());
            mqReceive.setProcessInstId(mqSend.getProcessInstId());
            mqReceive.setTaskId(mqSend.getTaskId());
            mqReceive.setTaskDefinitionKey(mqSend.getTaskDefinitionKey());
            String mapJson = mqSend.getMapJson();

            if(mapJson.contains("&quot;")){
                mqReceive.setMapJson(mapJson.replaceAll("&quot;","\""));
            }else{
                mqReceive.setMapJson(mapJson);
            }
            mqReceive.setProcessDefKey(mqSend.getProcessDefKey());
            mqReceive.setProcessInstId(mqSend.getProcessInstId());
            mqReceive.setTaskId(mqSend.getTaskId());
            mqReceive.setTaskDefinitionKey(mqSend.getTaskDefinitionKey());
            MqReceive target =  repository.save(mqReceive);

            handlerAction(target);
        }
        return true;
    }

    @Async(MULTI_THREAD_BEAN)
    public void handlerAction(MqReceive mqReceive){
        switch (mqReceive.getAction()){
            case ConstansAction.INSTANCESSTARTBYKEY:

                processInstancesService.instancesStart4Mq(mqReceive);
                break;
            case ConstansAction.TASKSCOMPLETE:

                processTasksServiceImpl.tasksComplete4Mq(mqReceive);
                break;
                default:
                    break;
        }
    }
}
