package com.simbest.boot.wfengine.config;/**
 * @author Administrator
 * @create 2019/12/9 11:41.
 */

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.simbest.boot.wfengine.process.listener.ProcessEventListener;
import com.simbest.boot.wfengine.process.listener.TaskEventListener;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 *@ClassName FlowableConfig
 *@Description 1.添加全局监听配置
 *@Author Administrator
 *@Date 2019/12/9 11:41
 *@Version 1.0
 **/
@Component
public class FlowableConfig implements ProcessEngineConfigurationConfigurer {

    @Autowired
    private TaskEventListener taskEventListener;
    @Autowired
    private ProcessEventListener processEventListener;

    /**
     * 添加全局监听
     * 任务创建，任务完成，任务分配办理人
     * 实例创建，实例完成
     * @param springProcessEngineConfiguration
     */
    @Override
    public void configure(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        //springboot中配置全局监听类
        Map<String, List<FlowableEventListener>> typedListeners = Maps.newHashMap();
        List<FlowableEventListener> flowableTaskEventListener = Lists.newArrayList();
        flowableTaskEventListener.add(taskEventListener);
        typedListeners.put("TASK_CREATED,TASK_COMPLETED,TASK_ASSIGNED", flowableTaskEventListener);
        List<FlowableEventListener> flowableProcessEventListener = Lists.newArrayList();
        flowableProcessEventListener.add(processEventListener);
        typedListeners.put("HISTORIC_PROCESS_INSTANCE_CREATED,HISTORIC_PROCESS_INSTANCE_ENDED", flowableProcessEventListener);
        springProcessEngineConfiguration.setTypedEventListeners(typedListeners);
    }
}
