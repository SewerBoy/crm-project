package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constants.Constants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;

import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.*;


import com.bjpowernode.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-08 14:37
 */
@Controller("tranController")
public class TranController {
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRemarkService transactionRemarkService;
    @Autowired
    private TransactionHistoryService transactionHistoryService;


    //跳转主页面
    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request) {
        //查询数据并封装
        List<DicValue> stages = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypes = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sources = dicValueService.queryDicValueByTypeCode("source");
        List<Transaction> transactionList = transactionService.selectAllTransaction();
        request.setAttribute("stages", stages);
        request.setAttribute("transactionTypes", transactionTypes);
        request.setAttribute("sources", sources);
        request.setAttribute("transactionList", transactionList);

        //转发
        return "workbench/transaction/index";
    }

    //跳转到save页面,并查询数据,渲染页面
    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request) {
        //查询并存放数据
        List<User> userList = userService.selectAllUser();
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("userList", userList);
        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);

        //转发
        return "workbench/transaction/save";
    }

    //通过名字模糊查询市场活动
    @RequestMapping("/workbench/transaction/queryActivityByVagueName.do")
    @ResponseBody
    public Object queryActivityByVagueName(String name) {
        //调用service层,查询数据
        List<Activity> activityList = activityService.queryActivityByVagueName(name);
        //响应结果
        return activityList;

    }

    //通过名字模糊查询联系人
    @RequestMapping("/workbench/transaction/queryContactsByVagueName.do")
    @ResponseBody
    public Object queryContactsByVagueName(String fullname) {
        //调用service层查询
        List<Contacts> contactsList = contactsService.queryContactsByVagueName(fullname);

        //响应数据
        return contactsList;

    }

    //处理可行性
    @RequestMapping("/workbench/transaction/getPossibility.do")
    @ResponseBody
    public Object getPossibility(String stageValue) {
        //解析可行性文件
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(stageValue);
        //响应数据
        return possibility;
    }

    //根据名称模糊查询客户
    @RequestMapping("/workbench/transaction/queryCustomerByName.do")
    @ResponseBody
    public Object queryCustomerByName(String name) {
        //调用service层查询数据
        List<Customer> customerList = customerService.queryCustomerByName(name);

        //返回查询数据
        return customerList;
    }

    //创建交易
    @RequestMapping("/workbench/transaction/saveCreateTransaction.do")
    @ResponseBody
    public Object saveCreateTransaction(@RequestParam Map<String, Object> map, HttpSession session) {
        //封装数据
        map.put(Constants.SESSION_USER, session.getAttribute(Constants.SESSION_USER));

        //调用service层保存数据
        ReturnObject ro = new ReturnObject();
       try{
           transactionService.saveTransaction(map);
           ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
       }catch(Exception e) {
           e.printStackTrace();
           ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
           ro.setMessage("系统繁忙,请稍后重试!");
        }

       //响应信息
        return ro;
    }

    //跳转交易详细页面
    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String id, HttpServletRequest request) {
        //调用service层查询数据
        Transaction transaction = transactionService.queryTransactionForDetailById(id);
        List<TransactionRemark> transactionRemarkList = transactionRemarkService.queryTransactionRemarkByTranId(id);
        List<TransactionHistory> transactionHistoryList= transactionHistoryService.queryTransactionHistoryByTranId(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        //给transaction添加属性,记录成功率
        ResourceBundle possibility = ResourceBundle.getBundle("possibility");
        String possibilityString = possibility.getString(transaction.getStage());
        transaction.setPossibility(possibilityString);

        //把数据放到作用域中
        request.setAttribute("transaction",transaction);
        request.setAttribute("transactionRemarkList",transactionRemarkList);
        request.setAttribute("transactionHistoryList",transactionHistoryList);
        request.setAttribute("stageList",stageList);

        return "workbench/transaction/detail";
    }
}

