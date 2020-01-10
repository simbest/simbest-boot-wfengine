package com.simbest.boot.provide.deployment.service.impl;/**
 * @author Administrator
 * @create 2019/12/3 16:46.
 */

import com.simbest.boot.api.BaseFlowableProcessApi;
import com.simbest.boot.provide.deployment.service.IProcessDeploymentService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 *@ClassName ProcessDeploymentServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/3 16:46
 *@Version 1.0
 **/
@Slf4j
@Service
public class ProcessDeploymentServiceImpl implements IProcessDeploymentService {

    @Autowired
    private BaseFlowableProcessApi baseFlowableProcessApi;

    protected ResourcePatternResolver resourcePatternResolver;

    /**
     * 自动部署专用
     * @param map
     * @return
     */
    @Override
    public int deploymentsAddInputStream(Map<String, Object> map) {
        String name = (String) map.get("name");
        String category = (String) map.get("category");
        String tenantId = (String) map.get("tenantId");
        MultipartFile file = (MultipartFile) map.get("file");
        String filename = (String) map.get("filename");
        try {
            if(name==null){
                name = "CustomAutoDeployment";
            }
            DeploymentBuilder builder = baseFlowableProcessApi.getRepositoryService().createDeployment().enableDuplicateFiltering().name(name);
            builder.addInputStream(filename,file.getInputStream());
            if (filename.endsWith(".bar") || filename.endsWith(".zip") || filename.endsWith(".jar")) {
                builder.addZipInputStream(new ZipInputStream(file.getInputStream()));
            } else {
                builder.addInputStream(filename,file.getInputStream());
            }
            if(category!=null){
                builder.category(category);
            }
            if(tenantId!=null){
                builder .tenantId(tenantId);
            }
            builder.deploy();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 查询列表
     * @param map
     * @return
     */
    @Override
    public List<Deployment> deploymentsQuery(Map<String, Object> map){
        String name = (String) map.get("name");
        String category = (String) map.get("category");
        String tenantId = (String) map.get("tenantId");
        int page = (int) map.get("page");
        int size = (int) map.get("size");
         List<Deployment> list = baseFlowableProcessApi.getRepositoryService().createDeploymentQuery()
                .deploymentCategoryLike(category)
                 .deploymentNameLike(name)
                 .deploymentTenantId(tenantId)
                 .orderByDeploymenTime()
                 .desc()
                 .listPage(page,size);
         return list;
    }

    /**
     * 获得一个部署
     * @param deploymentId
     * @return
     */
    @Override
    public Deployment deploymentsGet(String deploymentId) {
        return baseFlowableProcessApi.getRepositoryService().createDeploymentQuery().deploymentId(deploymentId).singleResult();
    }


}
