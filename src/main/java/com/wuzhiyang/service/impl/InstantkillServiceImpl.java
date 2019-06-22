package com.wuzhiyang.service.impl;

import com.wuzhiyang.dao.InstantkillDao;
import com.wuzhiyang.dao.SuccessKilledDao;
import com.wuzhiyang.dao.cache.RedisDao;
import com.wuzhiyang.dto.Exposer;
import com.wuzhiyang.dto.InskillExecution;
import com.wuzhiyang.entity.InstantKill;
import com.wuzhiyang.entity.SuccessKilled;
import com.wuzhiyang.enums.InstantStateEnum;
import com.wuzhiyang.exception.InskillCloseException;
import com.wuzhiyang.exception.InskillException;
import com.wuzhiyang.exception.RepeatKillException;
import com.wuzhiyang.service.InstantkillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wzy
 */
@Service
public class InstantkillServiceImpl implements InstantkillService {

    @Autowired
    private InstantkillDao instantkillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    private final Logger logger = LoggerFactory.getLogger(InstantkillServiceImpl.class);

    //md5盐值字符串,用于混淆MD5
    private final String salt = "sadkfjalsdjfalksj23423^&*^&%&!EBJKH#e™£4";

    @Override
    public List<InstantKill> getInstantkillList() {
        return instantkillDao.queryAll(0, 5);
    }

    @Override
    public InstantKill getById(long inskillId) {
        return instantkillDao.queryById(inskillId);
    }

    @Override
    public Exposer exportInskillUrl(long inskillId) {
        InstantKill instantKill = redisDao.getIntKill(inskillId);
        if (instantKill == null) {
            instantKill = instantkillDao.queryById(inskillId);
            if (instantKill == null) {
                return new Exposer(false, inskillId);
            } else {
                redisDao.putSeckill(instantKill);
            }
        }
        Date startTime = instantKill.getStartTime();
        Date endTime = instantKill.getEndTime();
        //系统当前时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, inskillId, nowTime.getTime(), startTime.getTime(),
                    endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(inskillId);
        return new Exposer(true, md5, inskillId);
    }

    @Override
    @Transactional
    public InskillExecution executeInskill(long inskillId, long userPhone, String md5) throws InskillException, RepeatKillException, InskillCloseException {
        if (md5 == null || !md5.equals(getMD5(inskillId))) {
            throw new InskillException("seckill data rewrite");
        }
        //执行秒杀逻辑:减库存 + 记录购买行为
        Date nowTime = new Date();

        try {
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(inskillId, userPhone);
            //唯一:seckillId,userPhone
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("inskill repeated");
            } else {
                //减库存,热点商品竞争
                int updateCount = instantkillDao.reduceNumber(inskillId, nowTime);
                if (updateCount <= 0) {
                    //没有更新到记录，秒杀结束,rollback
                    throw new InskillCloseException("inskill is closed");
                } else {
                    //秒杀成功 commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(inskillId, userPhone);
                    return new InskillExecution(inskillId, InstantStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (InskillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常 转化为运行期异常
            throw new InskillException("inskill inner error:" + e.getMessage());
        }
    }

    @Override
    public InskillExecution executeInskillProcedure(long inskillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(inskillId))) {
            return new InskillExecution(inskillId, InstantStateEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("instantkillId", inskillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        //执行存储过程,result被复制
        try {
            instantkillDao.killByProcedure(map);
            //获取result
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled sk = successKilledDao.
                        queryByIdWithSeckill(inskillId, userPhone);
                return new InskillExecution(inskillId, InstantStateEnum.SUCCESS, sk);
            } else {
                return new InskillExecution(inskillId, InstantStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new InskillExecution(inskillId, InstantStateEnum.INNER_ERROR);
        }
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

}
