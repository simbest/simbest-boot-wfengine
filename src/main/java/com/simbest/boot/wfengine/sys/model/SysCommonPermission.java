/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.wfengine.sys.model;

import com.simbest.boot.base.annotations.EntityIdPrefix;
import com.simbest.boot.base.enums.SysPermissionType;
import com.simbest.boot.base.model.LogicModel;
import com.simbest.boot.security.IPermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Objects;

/**
 * 用途：基础信息管理-权限管理
 * 作者: lishuyi
 * 时间: 2020/2/6  21:03
 */
@Data
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ApiModel(value = "基础信息管理-权限管理")
public class SysCommonPermission extends LogicModel implements IPermission {

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "P") //主键前缀，此为可选项注解
    private String id;

    @ApiModelProperty("系统权限编码")
    @Column(unique=true)
    private String permissionCode;

    @ApiModelProperty("描述")
    @Column
    private String description;

    @ApiModelProperty("路径")
    @Column
    private String url;

    @ApiModelProperty("图标")
    @Column
    private String icon;

    @ApiModelProperty("菜单级别")
    @Column
    private Integer menuLevel;

    @ApiModelProperty("排序值")
    @Column()
    private Integer displayOrder ;

    @ApiModelProperty("权限类型")
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SysPermissionType permissionType;

    @ApiModelProperty("父级id")
    @Column(length = 40)
    private String parentId;

    @ApiModelProperty("标记描述")
    @Column
    private String remark;

    @ApiModelProperty("应用编码")
    @Column
    private String appcode;//管理后台修改用户、角色的权限时用于判断是否已关联该权限

    @Transient
    private Boolean checked;//管理后台修改用户、角色的权限时用于判断是否已关联该权限

    @Override
    public String getAuthority() {
        return getPermissionCode();
    }

    @Override
    public String getType() {
        return getPermissionType().getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysCommonPermission dto = (SysCommonPermission) o;
        return Objects.equals(getPermissionCode(), dto.getPermissionCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPermissionCode());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
