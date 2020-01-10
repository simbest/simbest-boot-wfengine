package com.simbest.boot.process.management.service.impl;/**
 * @author Administrator
 * @create 2020/1/9 15:43.
 */

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *@ClassName GetPageable
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/9 15:43
 *@Version 1.0
 **/
public class GetPageable {

    public static Pageable getPageable(int page, int size, String direction, String properties) {
        int pagePage = page < 1 ? 0 : page - 1;
        int pageSize = size < 1 ? 1 : size;
        if (100 < pageSize) {
        }

        PageRequest pageable;
        if (StringUtils.isNotEmpty(direction) && StringUtils.isNotEmpty(properties)) {
            Sort.Direction sortDirection;
            try {
                direction = direction.toUpperCase();
                sortDirection = Sort.Direction.valueOf(direction);
            } catch (IllegalArgumentException var11) {
                var11.printStackTrace();
                sortDirection = Sort.Direction.ASC;
            }

            String[] sortProperties = properties.split(",");
            Sort sort = Sort.by(sortDirection, sortProperties);
            pageable = PageRequest.of(pagePage, pageSize, sort);
        } else {
            pageable = PageRequest.of(pagePage, pageSize);
        }

        return pageable;
    }
}
