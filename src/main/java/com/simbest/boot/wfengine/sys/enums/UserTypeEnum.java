/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.wfengine.sys.enums;

/**
 * 用途：用户类型枚举
 * 作者: lishuyi
 * 时间: 2019/11/13  21:10
 */
public enum UserTypeEnum {
    SYSUSER(1, "系统用户"),
    APPUSER(2, "业务用户");

    private int key;
    private String value;

    UserTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static UserTypeEnum valueOfKey(int key) {
        for (UserTypeEnum stateEnum : UserTypeEnum.values()) {
            if (stateEnum.key == key) {
                return stateEnum;
            }
        }
        throw new IllegalArgumentException("No element matches " + key);
    }

    public static UserTypeEnum valueOfValue(String value) {
        for (UserTypeEnum stateEnum : UserTypeEnum.values()) {
            if (stateEnum.value.equals(value)) {
                return stateEnum;
            }
        }
        throw new IllegalArgumentException("No element matches " + value);
    }

}
