package com.wuzhiyang.dao;

import com.wuzhiyang.entity.InstantKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * @author wzy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class InstantkillDaoTest {

    @Autowired
    private InstantkillDao instantkillDao;


    @Test
    public void reduceNumber() throws Exception {
        int updateCount = instantkillDao.reduceNumber(1000L, new Date());
        System.out.println("updateCount=" + updateCount);
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        InstantKill instantKill = instantkillDao.queryById(id);
        System.out.println(instantKill.getName());
        System.out.println(instantKill);
    }

    @Test
    public void queryAll() throws Exception {
        //Parameter 'offset' not found. Available parameters are [0, 1, param1, param2]
        // java没有保存形参的记录:queryAll(int offet,int limit) ->queryAll(arg0,arg1)
        List<InstantKill> instantKills = instantkillDao.queryAll(0, 100);
        for (InstantKill instantKill : instantKills) {
            System.out.println(instantKills);
        }
    }

    @Test
    public void killByProcedure() throws Exception {

    }

}