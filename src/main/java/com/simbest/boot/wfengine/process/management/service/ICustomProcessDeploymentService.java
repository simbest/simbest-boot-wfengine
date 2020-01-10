package com.simbest.boot.wfengine.process.management.service;

import com.simbest.boot.wfengine.process.management.dto.CustomProcessDeploymentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @author Administrator
 * @create 2020/1/3 17:32.
 */
public interface ICustomProcessDeploymentService  {
    Page<CustomProcessDeploymentDto> findAllCustom(Map<String,Object> mapParams, Pageable pageable);

    String deploymentsAdd(Map<String,Object> map);
}
