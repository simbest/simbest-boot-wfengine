/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.wfengine.sys.model;

import com.simbest.boot.base.annotations.EntityIdPrefix;
import com.simbest.boot.base.model.LogicModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 用途：基础信息管理-角色权限管理
 * 作者: lishuyi
 * 时间: 2020/2/6  19:11
 */
@Data
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ApiModel(value = "基础信息管理-角色权限管理")
public class SysCommonRolePermission extends LogicModel {

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "RP") //主键前缀，此为可选项注解
    private String id;

    @ApiModelProperty("角色id")
    @Column
    private String roleId;

    @ApiModelProperty("权限id")
    @Column
    private String permissionId;

}
