package com.simbest.boot.provide.deployment.service;/**
 * @author Administrator
 * @create 2019/12/3 16:45.
 */

import org.flowable.engine.repository.Deployment;

import java.util.List;
import java.util.Map;

/**
 *@ClassName IProcessDeploymentService
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/3 16:45
 *@Version 1.0
 **/
public interface IProcessDeploymentService {
    /**
     * 创建部署
     * @param map
     * @return
     */
    public int deploymentsAdd(Map<String,Object> map) ;

    /**
     * 查询列表
     * @param map
     * @return
     */
    List<Deployment> deploymentsQuery(Map<String,Object> map);

    /**
     * 获得一个部署
     * @param deploymentId
     * @return
     */
    Deployment deploymentsGet(String deploymentId);

    /**
     * 自动部署专用
     * @param map
     * @return
     */
    int deploymentsAddInputStream(Map<String,Object> map);
}
