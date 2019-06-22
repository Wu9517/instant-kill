package com.wuzhiyang.service.impl;

import com.wuzhiyang.dto.Exposer;
import com.wuzhiyang.dto.InskillExecution;
import com.wuzhiyang.entity.InstantKill;
import com.wuzhiyang.exception.InskillCloseException;
import com.wuzhiyang.exception.RepeatKillException;
import com.wuzhiyang.service.InstantkillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author wzy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class InstantkillServiceImplTest {

    @Autowired
    private InstantkillService instantkillService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void getInstantkillList() throws Exception {
        List<InstantKill> list = instantkillService.getInstantkillList();
        logger.info("list={}", list);
    }

    @Test
    public void getById() throws Exception {
        long id = 1000;
        InstantKill instantKill = instantkillService.getById(id);
        logger.info("instantKill={}", instantKill);
    }

    @Test
    public void exportInskillUrl() throws Exception {
        long id = 1001;
        Exposer exposer = instantkillService.exportInskillUrl(id);
        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            long phone = 13502171127L;
            String md5 = exposer.getMd5();
            try {
                InskillExecution execution = instantkillService.executeInskill(id, phone, md5);
                logger.info("result={}", execution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (InskillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            //秒杀未开启
            logger.warn("exposer={}", exposer);
        }
    }

    @Test
    public void executeInskillProcedure() throws Exception {
        long instantkillId = 1001;
        long phone = 1368011101;
        Exposer exposer = instantkillService.exportInskillUrl(instantkillId);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            InskillExecution execution = instantkillService.executeInskillProcedure(instantkillId, phone, md5);
            logger.info(execution.getStateInfo());
        }
    }

}