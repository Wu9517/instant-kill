package com.wuzhiyang.web;

import com.wuzhiyang.dto.Exposer;
import com.wuzhiyang.dto.InskillExecution;
import com.wuzhiyang.dto.InskillResult;
import com.wuzhiyang.entity.InstantKill;
import com.wuzhiyang.enums.InstantStateEnum;
import com.wuzhiyang.exception.InskillCloseException;
import com.wuzhiyang.exception.RepeatKillException;
import com.wuzhiyang.service.InstantkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangyijun on 15/11/4.
 */
@Controller
@RequestMapping("/inskill")
public class InskillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InstantkillService instantkillService;

    @RequestMapping(value = "/index")
    public String list() {
        return "index";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        //获取列表页
        List<InstantKill> list = instantkillService.getInstantkillList();
        model.addAttribute("list", list);
        return "list";
    }

    @RequestMapping(value = "/detail/{inskillId}", method = RequestMethod.GET)
    public String detail(@PathVariable("inskillId") Long inskillId, Model model) {
        if (inskillId == null) {
            return "redirect:/inskill/list";
        }
        InstantKill instantKill = instantkillService.getById(inskillId);
        if (instantKill == null) {
            return "forward:/inskill/list";
        }
        model.addAttribute("inskill", instantKill);
        return "detail";
    }

    //ajax json
    @RequestMapping(value = "/{inskillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public InskillResult<Exposer> exposer(@PathVariable Long inskillId) {
        InskillResult<Exposer> result;
        try {
            Exposer exposer = instantkillService.exportInskillUrl(inskillId);
            result = new InskillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new InskillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{inskillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public InskillResult<InskillExecution> execute(@PathVariable("inskillId") Long inskillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long phone) {
        //springmvc valid
        if (phone == null) {
            return new InskillResult<InskillExecution>(false, "未注册");
        }
        InskillResult<InskillExecution> result;
        try {
            //存储过程调用.
            InskillExecution execution = instantkillService.executeInskillProcedure(inskillId, phone, md5);
            return new InskillResult<InskillExecution>(true, execution);
        } catch (RepeatKillException e) {
            InskillExecution execution = new InskillExecution(inskillId, InstantStateEnum.REPEAT_KILL);
            return new InskillResult<InskillExecution>(true, execution);
        } catch (InskillCloseException e) {
            InskillExecution execution = new InskillExecution(inskillId, InstantStateEnum.END);
            return new InskillResult<InskillExecution>(true, execution);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            InskillExecution execution = new InskillExecution(inskillId, InstantStateEnum.INNER_ERROR);
            return new InskillResult<InskillExecution>(true, execution);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public InskillResult<Long> time() {
        Date now = new Date();
        return new InskillResult(true, now.getTime());
    }

}
