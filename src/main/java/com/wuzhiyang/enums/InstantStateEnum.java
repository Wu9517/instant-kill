package com.wuzhiyang.enums;

import lombok.Getter;

/**
 * @author wzy
 */
@Getter
public enum InstantStateEnum {

    SUCCESS(1, "秒杀开始"),
    END(2, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATA_REWRITE(-3, "数据篡改");

    private int state;
    private String stateInfo;

    InstantStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static InstantStateEnum stateOf(int index) {
        for (InstantStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
