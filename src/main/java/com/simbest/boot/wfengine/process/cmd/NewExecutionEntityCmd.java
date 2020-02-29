package com.simbest.boot.wfengine.process.cmd;/**
 * @author Administrator
 * @create 2020/2/28 14:57.
 */

import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.cmd.NeedsActiveExecutionCmd;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;

/**
 *@ClassName NewExecutionEntityCmd
 *@Description 创建ExecutionEntity，手动创建task前，先要创建ExecutionEntity
 *@Author Administrator
 *@Date 2020/2/28 14:57
 *@Version 1.0
 **/
public class NewExecutionEntityCmd extends NeedsActiveExecutionCmd<Boolean> {

    private ExecutionEntityImpl rootExecution;

    public NewExecutionEntityCmd(String executionId, ExecutionEntityImpl rootExecution) {
        super(executionId);
        this.rootExecution = rootExecution;
    }

    @Override
    protected Boolean execute(CommandContext commandContext, ExecutionEntity executionEntity) {
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();
        executionEntityManager.insert(rootExecution);
        return true;
    }
}
