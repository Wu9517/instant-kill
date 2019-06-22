package com.wuzhiyang.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author wzy
 */
@Data
@ToString
public class InstantKill {
    private long instantkillId;

    private String name;

    private int number;

    private Date startTime;

    private Date endTime;

    private Date createTime;

}
