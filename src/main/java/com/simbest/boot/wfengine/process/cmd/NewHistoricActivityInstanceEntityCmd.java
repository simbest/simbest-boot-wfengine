package com.simbest.boot.wfengine.process.cmd;/**
 * @author Administrator
 * @create 2020/2/28 16:35.
 */

import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.cmd.NeedsActiveTaskCmd;
import org.flowable.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

/**
 *@ClassName NewHistoricActivityInstanceEntityCmd
 *@Description 创建历史流程环节
 *@Author Administrator
 *@Date 2020/2/28 16:35
 *@Version 1.0
 **/
public class NewHistoricActivityInstanceEntityCmd extends NeedsActiveTaskCmd {

    public NewHistoricActivityInstanceEntityCmd(String taskId, HistoricActivityInstanceEntity historicActivityInstanceEntity) {
        super(taskId);
        this.historicActivityInstanceEntity = historicActivityInstanceEntity;
    }

    private HistoricActivityInstanceEntity historicActivityInstanceEntity;

    @Override
    protected Object execute(CommandContext commandContext, TaskEntity taskEntity) {

        CommandContextUtil.getHistoricActivityInstanceEntityManager().insert(historicActivityInstanceEntity);

        return true;
    }
}
