package com.simbest.boot.wfengine.rabbitmq.web;/**
 * @author Administrator
 * @create 2020/2/19 0:22.
 */

import com.simbest.boot.base.web.controller.LogicController;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitQueueInfo;
import com.simbest.boot.wfengine.rabbitmq.service.IRabbitQueueInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *@ClassName RabbitQueueInfoController
 *@Description TODO
 *@Author Administrator
 *@Date 2020/2/19 0:22
 *@Version 1.0
 **/
@Api(description = "RabbitQueueInfoController", tags = {"Rabbit管理"})
@Slf4j
@RestController
@RequestMapping("/queueInfo")
public class RabbitQueueInfoController  extends LogicController<RabbitQueueInfo,String> {
    private IRabbitQueueInfoService service;

    @Autowired
    public RabbitQueueInfoController(IRabbitQueueInfoService service) {
        super(service);
        this.service = service;
    }
}