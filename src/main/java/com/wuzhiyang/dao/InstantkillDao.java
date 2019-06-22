package com.wuzhiyang.dao;

import com.wuzhiyang.entity.InstantKill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wzy
 */
public interface InstantkillDao {
    /**
     * 减库存
     *
     * @param instantkillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("instantkillId") long instantkillId, @Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀对象
     *
     * @param instantkillId
     * @return
     */
    InstantKill queryById(@Param("instantkillId") long instantkillId);

    /**
     * 根据偏移量查询秒杀商品列表
     *
     * @param offset
     * @param limit
     * @return
     */
    List<InstantKill> queryAll(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /***
     * 使用存储过程执行秒杀
     * @param paramMap
     */
    void killByProcedure(Map<String, Object> paramMap);
}
