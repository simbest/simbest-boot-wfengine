package com.simbest.boot.wfengine.process.cmd;/**
 * @author Administrator
 * @create 2020/2/14 16:15.
 */

import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.FlowableEngineAgenda;
import org.flowable.engine.impl.cmd.NeedsActiveTaskCmd;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

import java.util.Date;
import java.util.Map;

/**
 *@ClassName CommonJumpTaskCmd
 *@Description 自由跳转
 *@Author Administrator
 *@Date 2020/2/14 16:15
 *@Version 1.0
 **/
public class CommonJumpTaskCmd extends NeedsActiveTaskCmd<Boolean> {

    protected String processId;//执行实例id
    protected String targetNodeId;//目标节点
    protected Map<String, Object> formData;//变量
    public CommonJumpTaskCmd(String taskId,   String processId, String targetNodeId, Map<String, Object> formData) {
        super(taskId);
        this.processId = processId;
        this.targetNodeId = targetNodeId;
        this.formData = formData;
    }


    @Override
    protected Boolean execute(CommandContext commandContext, TaskEntity task) {
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();
        ExecutionEntity rootExecution=  executionEntityManager.findById(task.getExecutionId());

        /**
         * 结束当前流程环节
         */
        CommandContextUtil.getActivityInstanceEntityManager().recordActivityEnd(rootExecution,"delete");
        CommandContextUtil.getHistoryManager().recordActivityEnd(rootExecution,"delete",new Date());

        /**
         *
         */
        Process process = ProcessDefinitionUtil.getProcess(rootExecution.getProcessDefinitionId());
        FlowElement targetFlowElement = process.getFlowElement(targetNodeId);
        rootExecution.setCurrentFlowElement(targetFlowElement);
        rootExecution.setVariable("inputUserId",formData.get("inputUserId"));
        FlowableEngineAgenda agenda = CommandContextUtil.getAgenda();
        agenda.planContinueProcessInCompensation(rootExecution);

        /**
         *
         */
        CommandContextUtil.getTaskService().deleteTask(task, true);
        CommandContextUtil.getHistoryManager().recordTaskEnd(task,rootExecution,"delete",new Date());
        return true;
    }

}
