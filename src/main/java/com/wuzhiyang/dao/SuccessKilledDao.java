package com.wuzhiyang.dao;

import com.wuzhiyang.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Created by zhangyijun on 15/10/5.
 */
public interface SuccessKilledDao {

    /**
     * 插入购买明细,可过滤重复
     *
     * @param instantkillId
     * @param userPhone
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("instantkillId") long instantkillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象实体
     *
     * @param instantkillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("instantkillId") long instantkillId, @Param("userPhone") long userPhone);

}
