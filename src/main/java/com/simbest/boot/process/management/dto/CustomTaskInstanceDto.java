package com.simbest.boot.process.management.dto;/**
 * @author Administrator
 * @create 2020/1/3 17:24.
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
 *@ClassName CustomTaskInstanceModel
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/3 17:24
 *@Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomTaskInstanceDto {
    @Id
    private String id;//id
    private String procDefId;//流程定义id
    private String taskDefKey;//环节id
    private String procInstId;//流程实例id
    private String executionId;//流程执行id
    private String parentTaskId;//父任务id
    private String name;//任务名称
    private String description;//任务描述
    private String owner;//所有人
    private String assignee;//当前办理人
    private String startTime;//开始时间
    private String endTime;//结束时间
    private BigInteger duration;//持续时间
    private String deleteReason;//终止原因
    private Integer priority;//优先级
    private String tenantId;//租户id

    public static CustomTaskInstanceDto toObject(Map map) {
        CustomTaskInstanceDto customTaskInstanceDto = new CustomTaskInstanceDto();
        customTaskInstanceDto.setId((String)map.get("id")==null?(String)map.get("ID"):(String)map.get("id"));
        customTaskInstanceDto.setProcDefId((String)map.get("procDefId")==null?(String)map.get("PROCDEFID"):(String)map.get("procDefId"));
        customTaskInstanceDto.setTaskDefKey((String)map.get("taskDefKey")==null?(String)map.get("TASKDEFKEY"):(String)map.get("taskDefKey"));
        customTaskInstanceDto.setProcInstId((String)map.get("procInstId")==null?(String)map.get("PROCINSTID"):(String)map.get("procInstId"));
        customTaskInstanceDto.setExecutionId((String)map.get("executionId")==null?(String)map.get("EXECUTIONID"):(String)map.get("executionId"));
        customTaskInstanceDto.setParentTaskId((String)map.get("parentTaskId")==null?(String)map.get("PARENTTASKID"):(String)map.get("parentTaskId"));
        customTaskInstanceDto.setName((String)map.get("name")==null?(String)map.get("NAME"):(String)map.get("name"));
        customTaskInstanceDto.setDescription((String)map.get("description")==null?(String)map.get("DESCRIPTION"):(String)map.get("description"));
        customTaskInstanceDto.setOwner((String)map.get("owner")==null?(String)map.get("OWNER"):(String)map.get("owner"));
        customTaskInstanceDto.setAssignee((String)map.get("assignee")==null?(String)map.get("ASSIGNEE"):(String)map.get("assignee"));
        Date startTime = map.get("startTime")==null?(Date)map.get("STARTTIME"):(Date)map.get("startTime");
        Date endTime = map.get("endTime")==null?(Date)map.get("ENDTIME"):(Date)map.get("endTime");
        customTaskInstanceDto.setStartTime(startTime!=null?DateUtil.getDate(startTime,DateUtil.timestampPattern1):null);
        customTaskInstanceDto.setEndTime(endTime!=null?DateUtil.getDate(endTime,DateUtil.timestampPattern1):null);
        customTaskInstanceDto.setDuration((BigInteger)map.get("duration")==null?(BigInteger)map.get("DURATION"):(BigInteger)map.get("duration"));
        customTaskInstanceDto.setDeleteReason((String)map.get("deleteReason")==null?(String)map.get("DELETEREASON"):(String)map.get("deleteReason"));
        customTaskInstanceDto.setPriority((Integer)map.get("priority")==null?(Integer)map.get("PRIORITY"):(Integer)map.get("priority"));
        customTaskInstanceDto.setTenantId((String)map.get("tenantId")==null?(String)map.get("TENANTID"):(String)map.get("tenantId"));

        return customTaskInstanceDto;
    }

    public static List toObject(List<Map<String,Object>> lists){
        List customTaskInstanceDtos = new ArrayList();
        for (Map map : lists) {
            CustomTaskInstanceDto customTaskInstanceDto =  CustomTaskInstanceDto.toObject(map);
            if (customTaskInstanceDto != null) {
                customTaskInstanceDtos.add(customTaskInstanceDto);
            }
        }
        return customTaskInstanceDtos;
    }

}
