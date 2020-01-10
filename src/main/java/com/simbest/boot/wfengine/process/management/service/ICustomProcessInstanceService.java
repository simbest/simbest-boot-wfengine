package com.simbest.boot.wfengine.process.management.service;

import com.simbest.boot.wfengine.process.management.dto.CustomProcessInstanceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @author Administrator
 * @create 2020/1/3 17:32.
 */
public interface ICustomProcessInstanceService  {
    Page<CustomProcessInstanceDto> findAllCustom(Map<String,Object> mapParams, Pageable pageable);
}
