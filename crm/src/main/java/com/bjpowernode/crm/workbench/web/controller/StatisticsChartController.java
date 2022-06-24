package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-12 18:44
 */
@Controller
public class StatisticsChartController {
    @Autowired
    private TransactionService transactionService;
    //转发到交易统计图表
    @RequestMapping("/workbench/chart/transaction/tranChart.do")
    public String tranChart() {
        return "workbench/chart/transaction/index";
    }

    //获取图表数据,返回给前端
    @RequestMapping("/workbench/chart/transaction/queryCountOfGroupByStage.do")
    @ResponseBody
    public Object   queryCountOfGroupByStage() {
        //调用service层查询数据
        List<FunnelVO> funnelVOList = transactionService.queryCountOfGroupByStage();
        System.out.println("funnelVOList=============================================="+funnelVOList);
        //返回结果
        return funnelVOList;
    }

}
