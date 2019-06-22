package com.wuzhiyang.service;

import com.wuzhiyang.dto.Exposer;
import com.wuzhiyang.dto.InskillExecution;
import com.wuzhiyang.entity.InstantKill;
import com.wuzhiyang.exception.InskillCloseException;
import com.wuzhiyang.exception.InskillException;
import com.wuzhiyang.exception.RepeatKillException;

import java.util.List;

/**
 * @author wzy
 */
public interface InstantkillService {
    /**
     * 查询所有秒杀记录
     *
     * @return
     */
    List<InstantKill> getInstantkillList();

    /**
     * 查询单个秒杀记录
     *
     * @param inskillId
     * @return
     */
    InstantKill getById(long inskillId);

    /**
     * 秒杀开启输出秒杀接口地址,
     * 否则输出系统时间和秒杀时间
     *
     * @param inskillId
     */
    Exposer exportInskillUrl(long inskillId);

    /**
     * 执行秒杀操作
     *
     * @param inskillId
     * @param userPhone
     * @param md5
     */
    InskillExecution executeInskill(long inskillId, long userPhone, String md5)
            throws InskillException, RepeatKillException, InskillCloseException;


    /**
     * 执行秒杀操作by 存储过程
     *
     * @param inskillId
     * @param userPhone
     * @param md5
     */
    InskillExecution executeInskillProcedure(long inskillId, long userPhone, String md5);
}
