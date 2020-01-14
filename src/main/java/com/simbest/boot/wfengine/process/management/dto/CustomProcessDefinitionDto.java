package com.simbest.boot.wfengine.process.management.dto;/**
 * @author Administrator
 * @create 2020/1/3 17:18.
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *@ClassName CustomProcessDefinitionModel
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/3 17:18
 *@Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomProcessDefinitionDto {
    private String id;//id
    private String category;//流程分类
    private String name;//流程名称
    private String key_;//流程key
    private Integer version;//版本号
    private String deploymentId;//部署id
    private String tenantId;//租户id

    public static CustomProcessDefinitionDto toObject(Map map) {
        CustomProcessDefinitionDto customProcessDefinitionDto = new CustomProcessDefinitionDto();
        customProcessDefinitionDto.setId((String)map.get("id")==null?(String)map.get("ID"):(String)map.get("id"));
        customProcessDefinitionDto.setCategory((String)map.get("category")==null?(String)map.get("CATEGORY"):(String)map.get("category"));
        customProcessDefinitionDto.setName((String)map.get("name")==null?(String)map.get("NAME"):(String)map.get("name"));
        customProcessDefinitionDto.setKey_((String)map.get("key_")==null?(String)map.get("KEY_"):(String)map.get("key_"));
        customProcessDefinitionDto.setVersion((Integer) map.get("version")==null?(Integer)map.get("VERSION"):(Integer)map.get("version"));
        customProcessDefinitionDto.setDeploymentId((String)map.get("deploymentId")==null?(String)map.get("DEPLOYMENTID"):(String)map.get("deploymentId"));
        customProcessDefinitionDto.setTenantId((String)map.get("tenantId")==null?(String)map.get("TENANTID"):(String)map.get("tenantId"));
        return customProcessDefinitionDto;
    }

    public static List toObject(List<Map<String,Object>> lists){
        List customProcessDefinitionDtos = new ArrayList();
        for (Map map : lists) {
            CustomProcessDefinitionDto customProcessDefinitionDto =  CustomProcessDefinitionDto.toObject(map);
            if (customProcessDefinitionDto != null) {
                customProcessDefinitionDtos.add(customProcessDefinitionDto);
            }
        }
        return customProcessDefinitionDtos;
    }
}
