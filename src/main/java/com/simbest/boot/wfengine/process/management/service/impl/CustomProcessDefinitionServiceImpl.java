package com.simbest.boot.wfengine.process.management.service.impl;/**
 * @author Administrator
 * @create 2020/1/3 17:42.
 */

import com.simbest.boot.wfengine.process.management.dto.CustomProcessDefinitionDto;
import com.simbest.boot.wfengine.process.management.service.ICustomProcessDefinitionService;
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
 *@ClassName CustomProcessDefinitionServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/3 17:42
 *@Version 1.0
 **/
@Slf4j
@Service
public class CustomProcessDefinitionServiceImpl  implements ICustomProcessDefinitionService {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CustomProcessDefinitionDto> findAllCustom(Map<String, Object> mapParams, Pageable pageable) {

        String fromInquireSQL=" ";//基础查询部分
        List<Object> params = new ArrayList<Object>();//参数
        /*1.基础sql*/
        String dataQuerySQL = "SELECT prodef.ID_ AS id, prodef.CATEGORY_ AS category, prodef.NAME_ AS name, prodef.KEY_ AS key_, " +
                "prodef.VERSION_ AS version, prodef.DEPLOYMENT_ID_ AS deploymentId, prodef.RESOURCE_NAME_ AS resourceName, " +
                "prodef.DGRM_RESOURCE_NAME_ AS dgrmResourceName, prodef.TENANT_ID_ AS tenantId " +
                "FROM act_re_procdef prodef  where 1 = 1 ";
        String countQuerySQL ="SELECT count(*) AS count FROM act_re_procdef prodef where 1=1 ";

        /*2.基础sql拼装查询参数*/
        if((String) mapParams.get("key_")!=null && !"".equals((String) mapParams.get("key_"))){
            fromInquireSQL = fromInquireSQL+"AND prodef.KEY_ = :key_";
        }
        if((String) mapParams.get("name")!=null && !"".equals((String) mapParams.get("name"))){
            fromInquireSQL = fromInquireSQL+"AND prodef.NAME_ = :name ";
        }
        if((String) mapParams.get("tenantId")!=null && !"".equals((String) mapParams.get("tenantId"))){
            fromInquireSQL = fromInquireSQL+"AND prodef.TENANT_ID_ = :tenantId ";
        }
        /*3拼装1和2和排序*/
        dataQuerySQL = dataQuerySQL + fromInquireSQL + " order by VERSION_ desc";
        countQuerySQL = countQuerySQL + fromInquireSQL;

        /*4，创建查询query*/
        Query dataQuery = entityManager.createNativeQuery(dataQuerySQL);
        /*5.查询结果转Map*/
        dataQuery.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Query countQuery = entityManager.createNativeQuery(countQuerySQL);
        /*6.Query拼装查询参数*/
        if((String) mapParams.get("key_")!=null && !"".equals((String) mapParams.get("key_"))){
            dataQuery.setParameter("key_", (String) mapParams.get("key_"));
            countQuery.setParameter("key_", (String) mapParams.get("key_"));
        }
        if((String) mapParams.get("name")!=null && !"".equals((String) mapParams.get("name"))){
            dataQuery.setParameter("name", (String) mapParams.get("name"));
            countQuery.setParameter("name", (String) mapParams.get("name"));
        }
        if((String) mapParams.get("tenantId")!=null && !"".equals((String) mapParams.get("tenantId"))){
            dataQuery.setParameter("name", (String) mapParams.get("name"));
            countQuery.setParameter("name", (String) mapParams.get("name"));
        }
        /*7.分页*/
        dataQuery.setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset())));
        dataQuery.setMaxResults(pageable.getPageSize());
        /*8.获得结果*/
        List<Map<String,Object>> resultList = dataQuery.getResultList();
        String totalRecords = countQuery.getSingleResult().toString();
        return new PageImpl(CustomProcessDefinitionDto.toObject(resultList), pageable, Long.valueOf(totalRecords));

    }
}
