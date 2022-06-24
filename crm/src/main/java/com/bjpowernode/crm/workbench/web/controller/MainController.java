package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-06 8:41
 */
@Controller
public class MainController {
    /**
     * 跳转到业务页面的小窗口
     * @return
     */
    @RequestMapping("/workbench/main/index.do")
    public String index() {
        //跳转到maim.index.jsp
        return "workbench/main/index";
    }
}
