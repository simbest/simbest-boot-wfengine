package com.simbest.boot.wfengine.process.management.service.impl;/**
 * @author Administrator
 * @create 2020/1/3 17:42.
 */

import com.simbest.boot.wfengine.process.management.dto.CustomTaskInstanceDto;
import com.simbest.boot.wfengine.process.management.service.ICustomTaskInstanceService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *@ClassName CustomTaskInstanceServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/3 17:42
 *@Version 1.0
 **/
@Slf4j
@Service
public class CustomTaskInstanceServiceImpl  implements ICustomTaskInstanceService {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CustomTaskInstanceDto> findAllCustom(Map<String, Object> mapParams, Pageable pageable) {

        String fromInquireSQL=" ";//基础查询部分
        List<Object> params = new ArrayList<Object>();//参数
        /*1.基础sql*/
                String dataQuerySQL = "SELECT  taskinst.ID_ AS id,  taskinst.PROC_DEF_ID_ AS procDefId,  " +
                "taskinst.TASK_DEF_KEY_ AS taskDefKey,  taskinst.PROC_INST_ID_ AS procInstId,  taskinst.EXECUTION_ID_ AS executionId,  " +
                "taskinst.PARENT_TASK_ID_ AS parentTaskId,  taskinst.NAME_ AS name,  taskinst.DESCRIPTION_ AS description,  " +
                "taskinst.OWNER_ AS OWNER,  taskinst.ASSIGNEE_ AS assignee,  taskinst.START_TIME_ AS startTime,  " +
                "taskinst.END_TIME_ AS endTime,  taskinst.DURATION_ AS duration,  taskinst.DELETE_REASON_ AS deleteReason,  " +
                "taskinst.PRIORITY_ AS priority,  taskinst.TENANT_ID_ AS tenantId FROM  act_hi_taskinst taskinst where 1 = 1 ";
        String countQuerySQL =" SELECT count(*) FROM act_hi_taskinst taskinst where 1=1 ";


        /*2.基础sql拼装查询参数*/
        if((String) mapParams.get("id")!=null && !"".equals((String) mapParams.get("id"))){
            fromInquireSQL = fromInquireSQL+"AND taskinst.ID_ = :id ";
        }
        if((String) mapParams.get("procInstId")!=null && !"".equals((String) mapParams.get("procInstId"))){
            fromInquireSQL = fromInquireSQL+"AND taskinst.PROC_INST_ID_ = :procInstId ";
        }
        if((String) mapParams.get("taskDefKey")!=null && !"".equals((String) mapParams.get("taskDefKey"))){
            fromInquireSQL = fromInquireSQL+"AND taskinst.TASK_DEF_KEY_ = :taskDefKey ";
        }
        if((String) mapParams.get("name")!=null && !"".equals((String) mapParams.get("name"))){
            fromInquireSQL = fromInquireSQL+"AND taskinst.NAME_ = :name ";
        }
        if((String) mapParams.get("assignee")!=null && !"".equals((String) mapParams.get("assignee"))){
            fromInquireSQL = fromInquireSQL+"AND taskinst.ASSIGNEE_ = :assignee ";
        }
        if((String) mapParams.get("tenantId")!=null && !"".equals((String) mapParams.get("tenantId"))){
            fromInquireSQL = fromInquireSQL+"AND taskinst.TENANT_ID_ = :tenantId ";
        }
        /*3拼装1和2和排序*/
        dataQuerySQL = dataQuerySQL + fromInquireSQL + " order by START_TIME_ desc";
        countQuerySQL = countQuerySQL + fromInquireSQL;

        /*4，创建查询query*/
        Query dataQuery = entityManager.createNativeQuery(dataQuerySQL);
        /*5.查询结果转Map*/
        dataQuery.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Query countQuery = entityManager.createNativeQuery(countQuerySQL);
        /*6.Query拼装查询参数*/
        if((String) mapParams.get("id")!=null && !"".equals((String) mapParams.get("id"))){
            dataQuery.setParameter("id", (String) mapParams.get("id"));
            countQuery.setParameter("id", (String) mapParams.get("id"));
        }
        if((String) mapParams.get("procInstId")!=null && !"".equals((String) mapParams.get("procInstId"))){
            dataQuery.setParameter("procInstId",(String) mapParams.get("procInstId"));
            countQuery.setParameter("procInstId",(String) mapParams.get("procInstId"));
        }
        if((String) mapParams.get("taskDefKey")!=null && !"".equals((String) mapParams.get("taskDefKey"))){
            dataQuery.setParameter("taskDefKey", (String) mapParams.get("taskDefKey"));
            countQuery.setParameter("taskDefKey", (String) mapParams.get("taskDefKey"));
        }
        if((String) mapParams.get("name")!=null && !"".equals((String) mapParams.get("name"))){
            dataQuery.setParameter("name", (String) mapParams.get("name"));
            countQuery.setParameter("name", (String) mapParams.get("name"));
        }
        if((String) mapParams.get("assignee")!=null && !"".equals((String) mapParams.get("assignee"))){
            dataQuery.setParameter("assignee", (String) mapParams.get("assignee"));
            countQuery.setParameter("assignee", (String) mapParams.get("assignee"));
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
        return new PageImpl(CustomTaskInstanceDto.toObject(resultList), pageable, Long.valueOf(totalRecords));
    }
}
