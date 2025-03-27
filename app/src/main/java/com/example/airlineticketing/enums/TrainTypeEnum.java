package com.example.airlineticketing.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public enum TrainTypeEnum {
    A0 ("空客飞机", 0),
    A1 ("波音飞机", 1),
    A2 ("其他飞机", 2)

    ;

    // 成员变量
    private String desc;
    private int code;

    // 构造方法
    private TrainTypeEnum(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    // 普通方法
    public static String getName(Integer code) {
        for (TrainTypeEnum c : TrainTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.desc;
            }
        }
        return null;
    }

    // 普通方法
    public static Integer getCode(String name) {
        for (TrainTypeEnum c : TrainTypeEnum.values()) {
            if (name.equals(c.desc)) {
                return c.code;
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
        for (TrainTypeEnum statusEnum : TrainTypeEnum.values()) {
            list.add(statusEnum.getDesc());
        }
        return list;
    }
    public static List<Integer> getCodeList() {
        List<Integer> list = new ArrayList<>();
        for (TrainTypeEnum statusEnum : TrainTypeEnum.values()) {
            list.add(statusEnum.getCode());
        }
        return list;
    }
}



