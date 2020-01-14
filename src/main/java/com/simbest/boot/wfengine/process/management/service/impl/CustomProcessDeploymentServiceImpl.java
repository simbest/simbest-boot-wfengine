package com.simbest.boot.wfengine.process.management.service.impl;/**
 * @author Administrator
 * @create 2020/1/3 17:42.
 */

import com.simbest.boot.wfengine.api.BaseFlowableProcessApi;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.wfengine.process.management.dto.CustomProcessDeploymentDto;
import com.simbest.boot.wfengine.process.management.service.ICustomProcessDeploymentService;
import com.simbest.boot.sys.model.SysFile;
import com.simbest.boot.sys.service.ISysFileService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.repository.DeploymentBuilder;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 *@ClassName CustomProcessDeploymentServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/3 17:42
 *@Version 1.0
 **/
@Slf4j
@Service
public class CustomProcessDeploymentServiceImpl implements ICustomProcessDeploymentService {

    @Autowired
    private BaseFlowableProcessApi baseFlowableProcessApi;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ISysFileService sysFileService;

    @Override
    public Page<CustomProcessDeploymentDto> findAllCustom(Map<String, Object> mapParams, Pageable pageable) {

        String fromInquireSQL=" ";//基础查询部分
        List<Object> params = new ArrayList<Object>();//参数
        /*1.基础sql*/
        String dataQuerySQL = "SELECT deployment.ID_ AS id, deployment.NAME_ AS name, " +
                "deployment.DEPLOY_TIME_ AS deployTime, deployment.TENANT_ID_ AS tenantId  FROM act_re_deployment deployment where 1 =1 ";
        String countQuerySQL =" SELECT count(*) AS count FROM act_re_deployment deployment where 1=1 ";

        /*2.基础sql拼装查询参数*/
        if((String) mapParams.get("name")!=null && !"".equals((String) mapParams.get("name"))){
            fromInquireSQL = fromInquireSQL+"AND deployment.NAME_ = :name ";
        }
        if((String) mapParams.get("tenantId")!=null && !"".equals((String) mapParams.get("tenantId"))){
            fromInquireSQL = fromInquireSQL+"AND deployment.TENANT_ID_ = :tenantId ";
        }
        /*3拼装1和2和排序*/
        dataQuerySQL = dataQuerySQL + fromInquireSQL + " order by DEPLOY_TIME_ desc";
        countQuerySQL = countQuerySQL + fromInquireSQL;

        /*4，创建查询query*/
        Query dataQuery = entityManager.createNativeQuery(dataQuerySQL);
        /*5.查询结果转Map*/
        dataQuery.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Query countQuery = entityManager.createNativeQuery(countQuerySQL);
        /*6.Query拼装查询参数*/
        if((String) mapParams.get("name")!=null && !"".equals((String) mapParams.get("name"))){
            dataQuery.setParameter("name", (String) mapParams.get("name"));
            countQuery.setParameter("name", (String) mapParams.get("name"));
        }
        if((String) mapParams.get("tenantId")!=null && !"".equals((String) mapParams.get("tenantId"))){
            dataQuery.setParameter("tenantId",(String) mapParams.get("tenantId"));
            countQuery.setParameter("tenantId",(String) mapParams.get("tenantId"));
        }
        /*7.分页*/
        dataQuery.setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset())));
        dataQuery.setMaxResults(pageable.getPageSize());
        /*8.获得结果*/
        List<Map<String,Object>> resultList = dataQuery.getResultList();
        String totalRecords = countQuery.getSingleResult().toString();
        return new PageImpl(CustomProcessDeploymentDto.toObject(resultList), pageable, Long.valueOf(totalRecords));


    }

//    /**
//     * 创建部署
//     * @param map
//     * @return
//     */
//    @Override
//    public int deploymentsAdd(Map<String, Object> map) {
//        MultipartFile file = (MultipartFile) map.get("file");
//        String name = (String) map.get("name");
//        String category = (String) map.get("category");
//        String tenantId = (String) map.get("tenantId");
//        ZipInputStream zipIn = null;
//        try {
//            zipIn = new ZipInputStream(file.getInputStream());
//            DeploymentBuilder builder = baseFlowableProcessApi.getRepositoryService().createDeployment();
//            builder.addZipInputStream(zipIn);
//            if(name!=null){
//                builder.name(name);
//            }
//            if(category!=null){
//                builder.category(category);
//            }
//            if(tenantId!=null){
//                builder .tenantId(tenantId);
//            }
//            builder .deploy();
//            return 1;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
    /**
     * 创建部署
     * @param map
     * @return
     */
    @Override
    public String deploymentsAdd(Map<String, Object> map) {
        SysFile[] files = (SysFile[]) map.get("files");
        String name = (String) map.get("name");
        String category = (String) map.get("category");
        String tenantId = (String) map.get("tenantId");
        ZipInputStream zipIn = null;
        String message = "";
        for(SysFile sysFile : files){
            File file = sysFileService.getRealFileById(sysFile.getId());

            try {
                zipIn = new ZipInputStream(new FileInputStream(file));
                DeploymentBuilder builder = baseFlowableProcessApi.getRepositoryService().createDeployment();
                builder.addZipInputStream(zipIn);
                if(name!=null){
                    builder.name(name);
                }
                if(category!=null){
                    builder.category(category);
                }
                if(tenantId!=null){
                    builder .tenantId(tenantId);
                }
                builder .deploy();
                message = message +file.getName()+"部署成功！；";
            } catch (IOException e) {
                message = message +file.getName()+"部署失败！；";
                log.error(Exceptions.getStackTraceAsString(e));
                e.printStackTrace();
            }
        }
        return message.substring(0,message.length()-1);

    }
}
