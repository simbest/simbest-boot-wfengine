package com.simbest.boot.wfengine.process.management.dto;/**
 * @author Administrator
 * @create 2020/1/3 17:22.
 */

import com.simbest.boot.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *@ClassName CustomProcessInstanceModel
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/3 17:22
 *@Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomProcessInstanceDto {
    @Id
    private String id;//id
    private String procInstId;//实例id
    private String businessKey;//业务实体key
    private String procDefId;//流程定义id
    private String startTime;//启动时间
    private String endTime;//结束时间
    private BigInteger duration;//时长
    private String startUserId;//发起人
    private String startActId;//发起环节
    private String endActId;//结束环节
    private String superProcessInstanceId;//父流程实例ID
    private String deleteReason;//终止说明
    private String tenantId;//租户id

    public static CustomProcessInstanceDto toObject(Map map) {
        CustomProcessInstanceDto customProcessInstanceDto = new CustomProcessInstanceDto();
        customProcessInstanceDto.setId((String)map.get("id")==null?(String)map.get("ID"):(String)map.get("id"));
        customProcessInstanceDto.setProcInstId((String)map.get("procInstId")==null?(String)map.get("PROCINSTID"):(String)map.get("procInstId"));
        customProcessInstanceDto.setBusinessKey((String)map.get("businessKey")==null?(String)map.get("BUSINESSKEY"):(String)map.get("businessKey"));
        customProcessInstanceDto.setProcDefId((String)map.get("procDefId")==null?(String)map.get("PROCDEFID"):(String)map.get("procDefId"));
        Date startTime = map.get("startTime")==null?(Date)map.get("STARTTIME"):(Date)map.get("startTime");
        Date endTime = map.get("endTime")==null?(Date)map.get("ENDTIME"):(Date)map.get("endTime");
        customProcessInstanceDto.setStartTime(startTime!=null?DateUtil.getDate(startTime,DateUtil.timestampPattern1):null);
        customProcessInstanceDto.setEndTime(endTime!=null?DateUtil.getDate(endTime,DateUtil.timestampPattern1):null);
        customProcessInstanceDto.setDuration((BigInteger)map.get("duration")==null?(BigInteger)map.get("DURATION"):(BigInteger)map.get("duration"));
        customProcessInstanceDto.setStartUserId((String)map.get("startUserId")==null?(String)map.get("STARTUSERID"):(String)map.get("startUserId"));
        customProcessInstanceDto.setStartActId((String)map.get("startActId")==null?(String)map.get("STARTACTID"):(String)map.get("startActId"));
        customProcessInstanceDto.setEndActId((String)map.get("endActId")==null?(String)map.get("ENDACTID"):(String)map.get("endActId"));
        customProcessInstanceDto.setSuperProcessInstanceId((String)map.get("superProcessInstanceId")==null?(String)map.get("ID"):(String)map.get("superProcessInstanceId"));
        customProcessInstanceDto.setDeleteReason((String)map.get("deleteReason")==null?(String)map.get("SUPERPROCESSINSTANCEID"):(String)map.get("deleteReason"));
        customProcessInstanceDto.setTenantId((String)map.get("tenantId")==null?(String)map.get("TENANTID"):(String)map.get("tenantId"));
        return customProcessInstanceDto;
    }

    public static List toObject(List<Map<String,Object>> lists){
        List customProcessInstanceDtos = new ArrayList();
        for (Map map : lists) {
            CustomProcessInstanceDto customProcessInstanceDto =  CustomProcessInstanceDto.toObject(map);
            if (customProcessInstanceDto != null) {
                customProcessInstanceDtos.add(customProcessInstanceDto);
            }
        }
        return customProcessInstanceDtos;
    }
}
