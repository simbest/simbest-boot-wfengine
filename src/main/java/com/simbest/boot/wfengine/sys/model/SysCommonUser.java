/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.wfengine.sys.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simbest.boot.base.annotations.EntityIdPrefix;
import com.simbest.boot.base.model.LogicModel;
import com.simbest.boot.security.IBloc;
import com.simbest.boot.security.ICorp;
import com.simbest.boot.security.IOrg;
import com.simbest.boot.security.IPermission;
import com.simbest.boot.security.IPosition;
import com.simbest.boot.security.IRole;
import com.simbest.boot.security.IUser;
import com.simbest.boot.security.IUserOrg;
import com.simbest.boot.security.MySimpleGrantedAuthority;
import com.simbest.boot.security.SimpleBloc;
import com.simbest.boot.security.SimpleCorp;
import com.simbest.boot.security.SimpleOrg;
import com.simbest.boot.security.SimpleUserOrg;
import com.simbest.boot.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 用途：基础信息管理-用户管理
 * 作者: lishuyi
 * 时间: 2020/2/6  15:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ApiModel(value = "基础信息管理-用户管理")
public class SysCommonUser extends LogicModel implements IUser {

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "U")
    private String id;

    @ApiModelProperty(value = "登录账号")
    @Column(nullable = false, unique = true, length = 40)
    private String username;

    @ApiModelProperty(value = "登录密码")
    @Column(nullable = false, length = 100)
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "真实姓名")
    @Column(nullable = false, length = 40)
    private String truename;

    @ApiModelProperty(value = "系统昵称")
    @Column(nullable = false, length = 40)
    private String nickname;

    @ApiModelProperty(value = "移动电话")
    @Column(unique = true, length = 15)
    private String preferredMobile;

    @Override
    public String getTelephoneNumber() {
        return null;
    }

    @Override
    public Integer getGenderDictValue() {
        return null;
    }

    @Override
    public Boolean getIsCmcc() {
        return null;
    }

    @Override
    public Integer getPositionLevel() {
        return null;
    }

    @Override
    public String getEmployeeNumber() {
        return null;
    }

    @ApiModelProperty(value = "电子邮箱")
    @Column(length = 50)
    private String email;

    @ApiModelProperty(value = "身份证")
    @Column(unique = true, length = 20)
    private String idCard;

    @ApiModelProperty(value = "性别")
    @Column
    private Integer gender;

    @ApiModelProperty(value = "民族")
    @Column
    private Integer nation;

    @ApiModelProperty(value = "用户类型")
    @Column
    private Integer userType;

    @ApiModelProperty(value = "家庭住址")
    @Column(length = 500)
    private String address;

    @ApiModelProperty(value = "用户显示顺序")
    @Column
    private Integer displayOrder;

    @ApiModelProperty(value = "照片")
    @Column
    private String photo;

    @ApiModelProperty(value = "压缩照片")
    @Column
    private String compressPhoto;

    @ApiModelProperty(value = "照片上传时间")
    @JsonFormat(pattern = DateUtil.timestampPattern1, timezone = "GMT+8")
    @Column
    private Date photoTime;

    @Column
    private String openid;

    @Column
    private String unionid;

    @ApiModelProperty(value = "在职状态")
    @Column
    private Integer dutyStatus;

    @ApiModelProperty(value = "入职时间")
    @JsonFormat(pattern = DateUtil.datePattern1, timezone = "GMT+8")
    @Column
    private Date dutyDate;

    @Transient
    private String currentBloc;

    @Transient
    private String currentCorp;

    //应用获取组织信息需要
    @Column
    private String belongCompanyCode;//所属公司编码

    @Transient
    private String belongCompanyName;

    @Column
    private String belongCompanyTypeDictValue;//所属公司类型，省公司、地市分公司、县/市区分公司

    @Transient
    private String belongCompanyTypeDictDesc;

    @Column
    private String belongDepartmentCode;//所属部门编码

    @Transient
    private String belongDepartmentName;

    @Column
    private String belongOrgCode;//所属组织编码

    @Transient
    private String belongOrgName;

    @Transient
    private String belongCompanyCodeParent;

    @Transient
    private String belongCompanyNameParent;


    @Column
    private String reserve1; //预留扩展字段１

    @Column
    private String reserve2; //预留扩展字段2

    @Column
    private String reserve3; //预留扩展字段3

    @Column
    private String reserve4; //预留扩展字段4

    @Column
    private String reserve5; //预留扩展字段5

    @ApiModelProperty(value = "是否可用")
    @Column(nullable = false)
    private Boolean enabled = true;

    @ApiModelProperty(value = "账户是否过期")
    @Column(nullable = false)
    private Boolean accountNonExpired = true;

    @ApiModelProperty(value = "账户是否锁定")
    @Column(nullable = false)
    private Boolean accountNonLocked = true;

    @ApiModelProperty(value = "密码是否过期")
    @Column(nullable = false)
    private Boolean credentialsNonExpired = true;

    @Transient
    private String currentCorpCode;

    @Transient
    private String currentBlocCode;

    @Transient
    private Set<? extends IBloc> authBlocs;

    @Transient
    private Set<? extends ICorp> authCorps;

    @Transient
    private Set<? extends IOrg> authOrgs;

    @Transient
    private Set<? extends IPosition> authPositions;

    @Transient
    private Set<? extends IPermission> authPermissions;

    @Transient
    private Set<? extends IRole> authRoles;

    @Transient
    private Set<GrantedAuthority> authorities;

    @Transient
    private Set<? extends IUserOrg> authUserOrgs;

    @Override
    public boolean isAccountNonExpired() {
        return getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }



    @Override
    public void setAuthBlocs(Set<SimpleBloc> authBlocs) {

    }

    @Override
    public void setAuthCorps(Set<SimpleCorp> authCorps) {

    }

    @Override
    public void setAuthOrgs(Set<SimpleOrg> authOrgs) {

    }

    @Override
    public void setAuthUserOrgs(Set<SimpleUserOrg> authUserOrgs) {

    }

    @Override
    public void addAppPositions(Set<? extends IPosition> positions) {

    }

    @Override
    public void addAppRoles(Set<? extends IRole> roles) {

    }

    @Override
    public void addAppPermissions(Set<? extends IPermission> permissions) {
    }

    @Override
    public void addAppAuthorities(Set<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> currentAuthorities = this.getAuthorities();
        //追加权限
        if(null == currentAuthorities ) {
            currentAuthorities = new HashSet<>();
        }
        for (GrantedAuthority source : authorities) {
            MySimpleGrantedAuthority authority = new MySimpleGrantedAuthority(source.getAuthority());
            currentAuthorities.add(authority);
        }
        this.setAuthorities(currentAuthorities);
    }
}
