package com.simbest.boot.wfengine.process.cmd;/**
 * @author Administrator
 * @create 2020/2/28 16:35.
 */

import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.cmd.NeedsActiveTaskCmd;
import org.flowable.engine.impl.persistence.entity.ActivityInstanceEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

/**
 *@ClassName NewActivityInstanceEntityCmd
 *@Description 创建流程环节
 *@Author Administrator
 *@Date 2020/2/28 16:35
 *@Version 1.0
 **/
public class NewActivityInstanceEntityCmd extends NeedsActiveTaskCmd {

    public NewActivityInstanceEntityCmd(String taskId, ActivityInstanceEntity activityInstanceEntity) {
        super(taskId);
        this.activityInstanceEntity = activityInstanceEntity;
    }

    private ActivityInstanceEntity activityInstanceEntity;

    @Override
    protected Object execute(CommandContext commandContext, TaskEntity taskEntity) {


        CommandContextUtil.getActivityInstanceEntityManager().insert(activityInstanceEntity);

        return true;
    }
}
