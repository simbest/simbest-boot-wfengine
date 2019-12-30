package com.simbest.boot.appManage.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simbest.boot.base.annotations.EntityIdPrefix;
import com.simbest.boot.base.model.LogicModel;
import com.simbest.boot.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 *@ClassName SysAppConfig
 *@Description TODO 应用管理-基本配置，
 * wfengine已经启用多租户，接入wfengine项目必须进行配置，分配客户端系统的code，url，token，和客户端所在的服务器ip作为ip访问白名单
 *@Author Administrator
 *@Date 2019/12/23 11:16
 *@Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ApiModel(value = "应用管理-基本配置")
@Table(name = "sys_app_config")
public class SysAppConfig extends LogicModel {
    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "APP")
    private String id;

    @ApiModelProperty(value = "编码")
    @Column(name = "appCode", nullable = false, length=200)
    private String appCode;//编码

    @ApiModelProperty(value = "名称")
    @Column(name = "appName", nullable = false, length=200)
    private String appName;//名称

    @ApiModelProperty(value = "系统名称")
    @Column(name = "appSystemName", nullable = false, length=200)
    private String appSystemName;//系统名称

    @ApiModelProperty(value = "url")
    @Column(name = "appUrl", nullable = false, length=200)
    private String appUrl;//url

    @ApiModelProperty(value = "系统token")
    @Column(name = "accessToken", nullable = false, length=200)
    private String accessToken;//系统token

    @ApiModelProperty(value = "token有效截至日期")
    @JsonFormat(pattern = DateUtil.timestampPattern1, timezone = "GMT+8")
    @Column(name = "periodValidity", nullable = false)
    private Date periodValidity;//token有效截至日期

    @ApiModelProperty(value = "授权ip")
    @Column(name = "whiteHost", nullable = true, length=200)
    private String whiteHost;//授权ip

}
