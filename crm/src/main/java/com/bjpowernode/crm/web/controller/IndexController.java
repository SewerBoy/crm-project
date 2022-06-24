package com.bjpowernode.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-01 17:44
 */
@Controller
public class IndexController {
    /**
     * 理论上,给Controller方法分配的URL:http://127.0.0.1:8080/crm/
     * 为了方便springmvc把以上都省去了
     * @return
     */
    @RequestMapping("/")
    public String index() {
        //请求转发
        return "index";
    }
}
