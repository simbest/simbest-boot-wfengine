/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.wfengine.sys.enums;

/**
 * 用途：用户性别枚举
 * 作者: lishuyi
 * 时间: 2019/11/13  21:10
 */
public enum UserGenderEnum {
    MALE(1, "男"),
    FEMALE(2, "女");

    private int key;
    private String value;

    UserGenderEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static UserGenderEnum valueOfKey(int key) {
        for (UserGenderEnum stateEnum : UserGenderEnum.values()) {
            if (stateEnum.key == key) {
                return stateEnum;
            }
        }
        throw new IllegalArgumentException("No element matches " + key);
    }

    public static UserGenderEnum valueOfValue(String value) {
        for (UserGenderEnum stateEnum : UserGenderEnum.values()) {
            if (stateEnum.value.equals(value)) {
                return stateEnum;
            }
        }
        throw new IllegalArgumentException("No element matches " + value);
    }

}
