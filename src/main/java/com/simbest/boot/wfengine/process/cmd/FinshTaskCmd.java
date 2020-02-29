package com.simbest.boot.wfengine.process.cmd;/**
 * @author Administrator
 * @create 2020/2/28 23:11.
 */

import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.cmd.NeedsActiveTaskCmd;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

import java.util.Date;

/**
 *@ClassName FinshTaskCmd
 *@Description 完成当前任务
 *@Author Administrator
 *@Date 2020/2/28 23:11
 *@Version 1.0
 **/
public class FinshTaskCmd extends NeedsActiveTaskCmd<Boolean> {

    public FinshTaskCmd(String taskId) {
        super(taskId);
    }

    @Override
    protected Boolean execute(CommandContext commandContext, TaskEntity taskEntity) {
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();
        ExecutionEntity rootExecution=  executionEntityManager.findById(taskEntity.getExecutionId());
        CommandContextUtil.getTaskService().deleteTask(taskEntity, true);
        CommandContextUtil.getHistoryManager().recordTaskEnd(taskEntity,rootExecution,"finsh",new Date());
        /**
         * 结束当前流程环节
         */
        CommandContextUtil.getActivityInstanceEntityManager().recordActivityEnd(rootExecution,"finsh");
        CommandContextUtil.getHistoryManager().recordActivityEnd(rootExecution,"finsh",new Date());
        executionEntityManager.delete(taskEntity.getExecutionId());
        return true;
    }
}
