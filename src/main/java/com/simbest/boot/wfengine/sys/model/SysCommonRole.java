/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.wfengine.sys.model;

import com.simbest.boot.base.annotations.EntityIdPrefix;
import com.simbest.boot.base.model.LogicModel;
import com.simbest.boot.security.IRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 用途：基础信息管理-角色管理
 * 作者: lishuyi
 * 时间: 2020/2/6  19:11
 */
@Data
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ApiModel(value = "基础信息管理-角色管理")
public class SysCommonRole extends LogicModel implements IRole {

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "R") //主键前缀，此为可选项注解
    private String id;

    @ApiModelProperty("系统角色编码")
    @Column(nullable = false)
    private String roleCode; //系统角色编码

    @ApiModelProperty("角色名称")
    @Column(nullable = false)
    private String roleName; //角色名称

    @ApiModelProperty("是否业务角色")
    @Column(nullable = false)
    private Boolean isApplicationRole; //是否业务角色，true为业务角色，false为内置角色，内置角色不可删除，管理后台添加的角色默认为业务角色

    @ApiModelProperty("排序值")
    @Column(nullable = false)
    private Integer displayOrder;

    @Override
    public String getAuthority() {
        return getRoleCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        SysCommonRole rhs = (SysCommonRole) obj;
        return new EqualsBuilder()
                .append(getRoleCode(), rhs.getRoleCode())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getRoleCode())
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
