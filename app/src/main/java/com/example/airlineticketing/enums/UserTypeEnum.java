package com.example.airlineticketing.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户类型（0:普通用户 1:管理员）
 */
public enum UserTypeEnum {
    User ("普通用户", 0),
    Admin ("管理员", 1),

    ;

    // 成员变量
    private String desc;
    private int code;

    // 构造方法
    private UserTypeEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    // 普通方法
    public static String getName(Integer code) {
        for (UserTypeEnum c : UserTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.desc;
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取列表
     * @return
     */
    public static List<String> getNameList() {
        List<String> list = new ArrayList<>();
        for (UserTypeEnum statusEnum : UserTypeEnum.values()) {
            list.add(statusEnum.getDesc());
        }
        return list;
    }
    public static List<Integer> getCodeList() {
        List<Integer> list = new ArrayList<>();
        for (UserTypeEnum statusEnum : UserTypeEnum.values()) {
            list.add(statusEnum.getCode());
        }
        return list;
    }
}



