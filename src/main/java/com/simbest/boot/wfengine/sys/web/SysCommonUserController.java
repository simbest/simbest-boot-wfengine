/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.wfengine.sys.web;

import com.simbest.boot.base.web.controller.LogicController;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.wfengine.sys.enums.UserTypeEnum;
import com.simbest.boot.wfengine.sys.model.SysCommonUser;
import com.simbest.boot.wfengine.sys.service.ISysCommonUserService;
import com.simbest.boot.util.security.LoginUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用途：基础信息管理-用户管理控制器
 * 作者: lishuyi
 * 时间: 2020/2/7  12:11
 */
@Slf4j
@RestController
@RequestMapping(value="/sys/user")
@Api(description = "SysCommonUserController", tags = {"基础信息管理-用户管理控制器"})
public class SysCommonUserController extends LogicController<SysCommonUser, String> {

    @Autowired
    private PasswordEncoder myBCryptPasswordEncoder;

    @Autowired
    private LoginUtils loginUtils;

    private ISysCommonUserService service;

    @Autowired
    public SysCommonUserController(ISysCommonUserService service) {
        super(service);
        this.service=service;
    }



}
