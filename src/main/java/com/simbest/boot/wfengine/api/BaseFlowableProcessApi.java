package com.simbest.boot.wfengine.api;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class BaseFlowableProcessApi {

    /**
     * 获取默认的流程引擎对象
     * @return
     */
    public ProcessEngine getDefaultProcessEngine(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getProcessEngineConfiguration().getAsyncExecutor().setAutoActivate(true);
        return processEngine;
    }

    /**
     * 这个服务提供了管理与控制部署
     * (deployments)与流程定义(process definitions)的操作
     * @return
     */
    public RepositoryService getRepositoryService(){
        RepositoryService repositoryService = getDefaultProcessEngine().getRepositoryService();
        return repositoryService;
    }

    /**
     * RuntimeService用于启动流程定义的新流程实例
     * @return
     */
    public RuntimeService getRuntimeService(){
        RuntimeService runtimeService = getDefaultProcessEngine().getRuntimeService();
        return runtimeService;
    }

    /**
     * 所有任务相关的东西都组织
     * @return
     */
    public TaskService getTaskService(){
        TaskService taskService = getDefaultProcessEngine().getTaskService();
        return taskService;
    }

    /**
     * FormService是可选服务。也就是说Flowable没有它也能很好地运行，而不必牺牲任何功能。这个服务引入
     * 了开始表单(start form)与任务表单(task form)的概念。 开始表单是在流程实例启动前显示的表单，而任务
     * 表单是用户完成任务时显示的表单。Flowable可以在BPMN 2.0流程定义中定义这些表单。表单服务通过简
     * 单的方式暴露这些数据。再次重申，表单不一定要嵌入流程定义，因此这个服务是可选的。
     * @return
     */
    public FormService getFormService(){
        FormService formService = getDefaultProcessEngine().getFormService();
        return formService;
    }

    /**
     * HistoryService暴露Flowable引擎收集的所有历史数据。当执行流程时，引擎会保存许多数据（可配置），
     * 例如流程实例启动时间、谁在执行哪个任务、完成任务花费的事件、每个流程实例的执行路径，等等。这个
     * 服务主要提供查询这些数据的能力。
     * @return
     */
    public HistoryService getHistoryService(){
        HistoryService historyService = getDefaultProcessEngine().getHistoryService();
        return historyService;
    }

    /**
     * ManagementService通常在用Flowable编写用户应用时不需要使用。它可以读取数据库表与表原始数据的
     * 信息，也提供了对作业(job)的查询与管理操作。Flowable中很多地方都使用作业，例如定时器(timer)，异
     * 步操作(asynchronous continuation)，延时暂停/激活(delayed suspension/activation)等等。后续会详细
     * 介绍这些内容。
     * @return
     */
    public ManagementService getManagementServices(){
        ManagementService managementService = getDefaultProcessEngine().getManagementService();
        return managementService;
    }

    /**
     *IdentityService很简单。它用于管理（创建，更新，删除，查询……）组与用户。请注意，Flowable实际
     *        上在运行时并不做任何用户检查。例如任务可以分派给任何用户，而引擎并不会验证系统中是否存在该用
     *        户。这是因为Flowable有时要与LDAP、Active Directory等服务结合使用。
     * @return
     */
    public IdentityService getIdentityService(){
        IdentityService identityService = getDefaultProcessEngine().getIdentityService();
        return identityService;
    }

    /**
     * DynamicBpmnService可用于修改流程定义中的部分内容，而不需要重新部署它。例如可以修改流程定义
     * 中一个用户任务的办理人设置，或者修改一个服务任务中的类名
     * @return
     */
    public DynamicBpmnService getDynamicBpmnService(){
        DynamicBpmnService dynamicBpmnService =
                getDefaultProcessEngine().getDynamicBpmnService();
        return dynamicBpmnService;
    }
}
