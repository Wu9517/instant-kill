package com.wuzhiyang.dto;


import com.wuzhiyang.entity.SuccessKilled;
import com.wuzhiyang.enums.InstantStateEnum;
import lombok.Data;
import lombok.ToString;

/**
 * 封装秒杀执行后结果
 * Created by zhangyijun on 15/10/16.
 */
@Data
@ToString
public class InskillExecution {

    private long inskillId;

    //秒杀执行结果状态
    private int state;

    //状态表示
    private String stateInfo;

    //秒杀成功对象
    private SuccessKilled successKilled;


    public InskillExecution(long inskillId, InstantStateEnum statEnum, SuccessKilled successKilled) {
        this.inskillId = inskillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public InskillExecution(long seckillId, InstantStateEnum statEnum) {
        this.inskillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }
}
