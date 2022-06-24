package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.constants.Constants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-01 21:02
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * url和controller方法处理完成请求后，响应信息返回的页面的资源目录保持一致
     * @return
     */
    @RequestMapping("/settings/qx/user/tologin.do")
    public String tologin() {
        //2022年5月1日21:47:34     哈哈,他妈的,终于出页面了,成了,好好洗个澡,睡个好觉~！
        //请求转发到登录页面
        return "settings/qx/user/login";
    }

    /**
     * 用户登录验证
     * @param loginAct
     * @param loginPwd
     * @param isRemPwd
     * @return
     */
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd,
                        HttpServletRequest request, HttpSession session,
                        HttpServletResponse response) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        map.put("isRemPwd", isRemPwd);
        //调用Service层方,查询用户
        User user = userService.queryUserByLoginActAndloginPwd(map);
        //根据查询结果,生成响应信息
        ReturnObject returnObject = new ReturnObject();
        if (user == null) {
            //登录失败,用户名或密码错误,请从新登录
            returnObject.setCode("0");
            returnObject.setMessage("用户名或密码错误,请从新登录");
        }else {//进一步判断账号是否合法
            String nowStr = DateUtils.formateDateTime(new Date());//把当前时间转换为字符串进行比较
            if(nowStr.compareTo(user.getExpireTime()) > 0){
                //登录失败,账号已过期
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            }else if("0".equals(user.getLockState())) {
                //登录失败,状态被锁定
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁定");
            }else if(!user.getAllowIps().contains(request.getRemoteAddr())) {
                //登录失败,ip受限
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登录失败,ip受限");
            }else {
                //登录成功
                //把user放入session中
                session.setAttribute(Constants.SESSION_USER, user);
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);//封装数据

                //如果需要记住密码,则往外写cookie
                if("true".equals(isRemPwd)) {//当勾选"记住密码"时
                    Cookie cookie1 = new Cookie("loginAct", loginAct);
                    Cookie cookie2 = new Cookie("loginPwd", loginPwd);
                    cookie1.setMaxAge(10*24*60*60);
                    cookie2.setMaxAge(10*24*60*60);
                    response.addCookie(cookie1);
                    response.addCookie(cookie2);
                }else {//当不勾选时
                    Cookie cookie1 = new Cookie("loginAct", loginAct);
                    Cookie cookie2 = new Cookie("loginPwd", loginPwd);
                    cookie1.setMaxAge(0);
                    cookie2.setMaxAge(0);
                    response.addCookie(cookie1);
                    response.addCookie(cookie2);
                }


            }
        }
        return returnObject;//发送数据到前端,供前端使用
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session) {
        //清除cookie
        Cookie cookie1 = new Cookie("loginAct", "1");
        Cookie cookie2 = new Cookie("loginPwd", "1");
        cookie1.setMaxAge(0);
        cookie2.setMaxAge(0);
        response.addCookie(cookie1);
        response.addCookie(cookie2);
        //销毁session
        session.invalidate();
        System.out.println("settings/qx/user/logout.do");
        //从定向首页
        return "redirect:/";
    }


}
