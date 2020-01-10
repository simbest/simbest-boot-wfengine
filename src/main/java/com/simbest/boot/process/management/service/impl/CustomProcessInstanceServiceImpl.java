package com.simbest.boot.process.management.service.impl;/**
 * @author Administrator
 * @create 2020/1/3 17:42.
 */

import com.simbest.boot.process.management.dto.CustomProcessInstanceDto;
import com.simbest.boot.process.management.service.ICustomProcessInstanceService;
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
 *@ClassName CustomProcessInstanceServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/3 17:42
 *@Version 1.0
 **/
@Slf4j
@Service
public class CustomProcessInstanceServiceImpl implements ICustomProcessInstanceService {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CustomProcessInstanceDto> findAllCustom(Map<String, Object> mapParams, Pageable pageable) {

        String fromInquireSQL=" ";//基础查询部分
        List<Object> params = new ArrayList<Object>();//参数
        /*1.基础sql*/
        String dataQuerySQL = "SELECT  procinst.ID_ AS id,  procinst.PROC_INST_ID_ AS procInstId,  procinst.BUSINESS_KEY_ AS businessKey,  " +
                "procinst.PROC_DEF_ID_ AS procDefId,  procinst.START_TIME_ AS startTime,  procinst.END_TIME_ AS endTime,  procinst.DURATION_ AS duration,  " +
                "procinst.START_USER_ID_ AS startUserId,  procinst.START_ACT_ID_ AS startActId,  procinst.END_ACT_ID_ AS endActId,  " +
                "procinst.SUPER_PROCESS_INSTANCE_ID_ AS superProcessInstanceId,  procinst.DELETE_REASON_ AS deleteReason,  " +
                "procinst.TENANT_ID_ AS tenantId FROM  act_hi_procinst procinst where 1=1";
        String countQuerySQL =" SELECT count(*) FROM act_hi_procinst procinst where 1=1";


        /*2.基础sql拼装查询参数*/
        if((String) mapParams.get("procInstId")!=null && !"".equals((String) mapParams.get("procInstId"))){
            fromInquireSQL = fromInquireSQL+"AND procinst.PROC_INST_ID_ = :procInstId ";
        }
        if((String) mapParams.get("businessKey")!=null && !"".equals((String) mapParams.get("businessKey"))){
            fromInquireSQL = fromInquireSQL+"AND procinst.BUSINESS_KEY_ = :businessKey ";
        }
        if((String) mapParams.get("tenantId")!=null && !"".equals((String) mapParams.get("tenantId"))){
            fromInquireSQL = fromInquireSQL+"AND procinst.TENANT_ID_ = :tenantId ";
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
        if((String) mapParams.get("procInstId")!=null && !"".equals((String) mapParams.get("procInstId"))){
            dataQuery.setParameter("procInstId", (String) mapParams.get("procInstId"));
            countQuery.setParameter("procInstId", (String) mapParams.get("procInstId"));
        }
        if((String) mapParams.get("businessKey")!=null && !"".equals((String) mapParams.get("businessKey"))){
            dataQuery.setParameter("businessKey",(String) mapParams.get("businessKey"));
            countQuery.setParameter("businessKey",(String) mapParams.get("businessKey"));
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
        return new PageImpl(CustomProcessInstanceDto.toObject(resultList), pageable,  Long.valueOf(totalRecords));
    }
}
