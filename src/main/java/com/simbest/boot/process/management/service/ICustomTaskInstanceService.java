package com.simbest.boot.process.management.service;

import com.simbest.boot.process.management.dto.CustomTaskInstanceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @author Administrator
 * @create 2020/1/3 17:31.
 */
public interface ICustomTaskInstanceService {
    Page<CustomTaskInstanceDto> findAllCustom(Map<String,Object> mapParams, Pageable pageable);
}
