package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constants.Constants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.mapper.ClueActivityRelationMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.mysql.jdbc.TimeUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-26 15:52
 */
@Controller
public class ClueController {
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;


    /**
     * 跳转页面
     * @param request
     * @return
     */
    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request) {
        //调用service层查询数据
        List<User> userList = userService.selectAllUser();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        //存入数据
        request.setAttribute("userList", userList);
        request.setAttribute("appellationList", appellationList);
        request.setAttribute("clueStateList", clueStateList);
        request.setAttribute("sourceList", sourceList);

        //转发到页面
        return "workbench/clue/index";
    }

    /**
     * 保存创建的线索
     * @param clue
     * @param session
     * @return
     */
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session) {
        //封装参数
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));

        //调用service层，保存数据
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = clueService.saveClue(clue);
            if(i>0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(clue);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙,请稍后重试!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙,请稍后重试!");
        }

        return returnObject;
    }

    /**
     * 条件查询线索
     * @param fullname
     * @param company
     * @param phone
     * @param source
     * @param owner
     * @param mphone
     * @param state
     * @return
     */
    @RequestMapping("/workbench/clue/queryClueByCondition.do")
    @ResponseBody
    public Object queryClueByCondition(String fullname, String company, String phone,
                                       String source, String owner, String mphone,
                                       String state) {
        //封装参数
        Map map = new HashMap();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);

        //调用service层查询
        //获取具体clue
        List<Clue> clues = clueService.queryClueByCondition(map);

        //获取clue条目数
        int count = clueService.queryClueCountByCondition(map);

        //封装
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("clues", clues);
        retMap.put("count", count);
        return retMap;
    }


    /**
     * 查询线索详细信息
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id, HttpServletRequest request) {
        Clue clue = clueService.queryClueById(id);
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);
        List<ClueRemark> clueRemarks = clueRemarkService.queryClueRemarkForDetailByClueId(id);

        request.setAttribute("clue", clue);
        request.setAttribute("activityList", activityList);
        request.setAttribute("clueRemarks", clueRemarks);
        return "workbench/clue/detail";
    }


    /**
     * 根据id个name模糊查询市场活动
     * @param name
     * @param id
     * @return
     */
    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String name, String id) {
        //封装参数
        Map map = new HashMap();
        map.put("name", name);
        map.put("id", id);

        //调用service层
        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);

        //根据查询结果返回响应信息
        return activityList;
    }

    /**
     * 关联市场活动
     * @param activityId
     * @param clueId
     * @return
     */
    @RequestMapping("/workbench/clue/saveBund.do")
    @ResponseBody
    public Object saveBund(String[] activityId, String clueId) {
        //封装数据
        List<ClueActivityRelation> carList = new ArrayList<>();
        for (int i = 0; i < activityId.length; i++) {
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtils.getUUID());
            car.setClueId(clueId);
            car.setActivityId(activityId[i]);
            carList.add(car);
        }
        /*for(String id : activityId) {
            System.out.println("'2");
            car.setId(UUIDUtils.getUUID());
            car.setClueId(clueId);
            car.setActivityId(id);
            carList.add(car);
        }*/
        //调用service保存ClueActivityRelation
        ReturnObject ro = new ReturnObject();
        try {
            int i = clueActivityRelationService.saveClueActivityRelation(carList);
            if(i > 0) {
                List<Activity> activityList = activityService.queryActivityBatchByIds(activityId);
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                ro.setRetData(activityList);
            }else {
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统繁忙,请稍后从重试!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙,请稍后从重试!");
        }

        //返回结果
        return ro;


    }

    //删除线索市场活动关系
    @RequestMapping("/workbench/clue/deleteClueActivityRelation.do")
    @ResponseBody
    public Object deleteClueActivityRelation(String activityId, String clueId) {
        //封装数据
      ClueActivityRelation car = new ClueActivityRelation();
      car.setClueId(clueId);
      car.setActivityId(activityId);

        //调用service层
        ReturnObject ro = new ReturnObject();
        try {
            int i = clueActivityRelationService.deleteClueActivityRelation(car);
            System.out.println("i==="+i);
            if(i > 0) {
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统繁忙,请稍后重试!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙,请稍后重试!");
        }

        //返回结果
        return ro;
    }

    //转换按钮
    @RequestMapping("/workbench/clue/transition.do")
    public String transition(String id, HttpServletRequest request) {
        //调用service层查询数据
        Clue clue = clueService.queryClueById(id);
        request.setAttribute("clue", clue);
        List<DicValue> dicValues = dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("dicValues", dicValues);

        return "workbench/clue/convert";
    }

    //搜索市场活动
    @RequestMapping("/workbench/clue/searchActivityForNameClueId.do")
    @ResponseBody
    public Object searchActivityForNameClueId(String name, String clueId) {
        System.out.println("name==="+name);
        System.out.println("clueId==="+clueId);
        //封装参数
        Map map = new HashMap<String, String>();
        map.put("name", name);
        map.put("clueId", clueId);

        //调用service层查询数据
        List<Activity> activityList = activityService.queryActivityForActivityNameClueId(map);
        System.out.println("al==="+activityList);
        //返回查询结果
        return activityList;
    }

    //转换
    @RequestMapping("/workbench/clue/convertActivityClue.do")
    @ResponseBody
    public Object convertActivityClue(String clueId, String money, String name, String expectedDate,
                                      String stage, String source,String isCreateTran, String activityId, HttpSession session) {
        //封装数据
        Map map = new HashMap<String, Object>();
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        map.put("clueId", clueId);
        map.put(Constants.SESSION_USER, user);
        map.put("name", name);
        map.put("money", money);
        map.put("expectedDate", expectedDate);
        map.put("stage", stage);
        map.put("source", source);
        map.put("activityId", activityId);
        map.put("isCreateTran", isCreateTran);


        //调用service层
        ReturnObject ro = new ReturnObject();
        try {
            //保存用户
            clueService.saveConvert(map);
            ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙,请稍后重试!");
        }

        return ro;
    }

    //保存线索备注
    @RequestMapping("/workbench/clue/saveClueRemark.do")
    @ResponseBody
    public Object saveClueRemark(ClueRemark clueRemark, HttpSession session) {
        //封装数据
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        clueRemark.setId(UUIDUtils.getUUID());
        clueRemark.setCreateBy(user.getId());
        clueRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        clueRemark.setEditFlag(Constants.REMARK_EDIT_FLAG_NO_EDITED);
        //调用service层保存数据
        ReturnObject ro = new ReturnObject();
        try {
            int count = clueRemarkService.saveClueRemark(clueRemark);
            if(count > 0) {
                List<ClueRemark> clueRemarks = clueRemarkService.queryClueRemarkByClueId(clueRemark.getClueId());
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                ro.setRetData(clueRemarks);
            }else {
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统繁忙,请稍后重试!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙,请稍后重试!");
        }

        //返回结果
        return ro;
    }

}
