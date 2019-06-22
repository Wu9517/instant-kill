package com.wuzhiyang.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author wzy
 */
@Data
@ToString
public class SuccessKilled {
    private long instantkillId;

    private long userPhone;

    private short state;

    private Date createTime;

    // 多对一
    private InstantKill instantKill;
}
