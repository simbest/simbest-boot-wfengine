package com.simbest.boot.process.management.dto;/**
 * @author Administrator
 * @create 2020/1/3 17:21.
 */

import com.simbest.boot.sys.model.SysFile;
import com.simbest.boot.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *@ClassName CustomProcessDeploymentModel
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/3 17:21
 *@Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomProcessDeploymentDto {
    private String id;//id
    private String name;//名称
    private String deployTime;//部署时间
    private String tenantId;//租户id

    private String category;//

    @Transient
    private SysFile[] file;

    public static CustomProcessDeploymentDto toObject(Map map) {
        CustomProcessDeploymentDto customProcessDeploymentDto = new CustomProcessDeploymentDto();
        customProcessDeploymentDto.setId((String)map.get("id")==null?(String)map.get("ID"):(String)map.get("id"));
        customProcessDeploymentDto.setName((String)map.get("name")==null?(String)map.get("NAME"):(String)map.get("name"));
        Date deployTime = map.get("deployTime")==null?(Date)map.get("DEPLOYTIME"):(Date)map.get("deployTime");
        customProcessDeploymentDto.setDeployTime(deployTime!=null?DateUtil.getDate(deployTime,DateUtil.timestampPattern1):null);
        customProcessDeploymentDto.setTenantId((String)map.get("tenantId")==null?(String)map.get("TENANTID"):(String)map.get("tenantId"));
        return customProcessDeploymentDto;
    }

    public static List toObject(List<Map<String,Object>> lists){
        List customProcessDeploymentDtos = new ArrayList();
        for (Map map : lists) {
            CustomProcessDeploymentDto customProcessDeploymentDto =  CustomProcessDeploymentDto.toObject(map);
            if (customProcessDeploymentDto != null) {
                customProcessDeploymentDtos.add(customProcessDeploymentDto);
            }
        }
        return customProcessDeploymentDtos;
    }
}
