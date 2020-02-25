package com.simbest.boot.wfengine.provide.processDefinitions.service;/**
 * @author Administrator
 * @create 2019/12/3 18:48.
 */

import org.flowable.engine.repository.ProcessDefinition;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 *@ClassName IProcessDefinitionsService
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/3 18:48
 *@Version 1.0
 **/
public interface IProcessDefinitionsService {

    /**
     * 获得一个流程定义
     * @param processDefinitionId
     * @return
     */
    public ProcessDefinition definitionsGet(String processDefinitionId) ;

    /**
     * 流程定义列表
     * @param map
     * @return
     */
    List<ProcessDefinition> definitionsQuery(Map<String,Object> map);

    /**
     * 获取流程图
     * @param processDefinitionId
     * @param processInstanceId
     * @return
     */
    InputStream getDiagram(String processDefinitionId, String processInstanceId);
}
