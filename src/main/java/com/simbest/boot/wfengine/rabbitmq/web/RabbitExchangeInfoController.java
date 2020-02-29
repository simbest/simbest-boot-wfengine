package com.simbest.boot.wfengine.rabbitmq.web;/**
 * @author Administrator
 * @create 2020/2/19 0:22.
 */

import com.simbest.boot.base.web.controller.LogicController;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitExchangeInfo;
import com.simbest.boot.wfengine.rabbitmq.service.IRabbitExchangeInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *@ClassName RabbitExchangeInfoController
 *@Description TODO
 *@Author Administrator
 *@Date 2020/2/19 0:22
 *@Version 1.0
 **/
@Api(description = "RabbitExchangeInfoController", tags = {"Rabbit管理"})
@Slf4j
@RestController
@RequestMapping("/exchangeInfo")
public class RabbitExchangeInfoController  extends LogicController<RabbitExchangeInfo,String> {
    private IRabbitExchangeInfoService service;

    @Autowired
    public RabbitExchangeInfoController(IRabbitExchangeInfoService service) {
        super(service);
        this.service = service;
    }
}
