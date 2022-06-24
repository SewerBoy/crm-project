package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constants.Constants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.sun.deploy.net.HttpResponse;
/*import org.apache.logging.log4j.core.util.UuidUtil;*/
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.*;
import java.util.*;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-06 13:41
 */
@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;

    //查看活动
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        //获取查询数据
        List<User> userList = userService.selectAllUser();
        //把数据保存到session中
        HttpSession session = request.getSession();
        session.setAttribute("userList", userList);
        return "workbench/activity/index";
    }

    /**
     * 添加市场活动
     * @param activity
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/saveCreanteActivity.do")
    @ResponseBody
    public Object saveCreanteActivity(Activity activity, HttpSession session) {
        //封装数据
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        String date = DateUtils.formatDateTime(new Date());
        activity.setId(UUIDUtils.getUUID());//活动id
        activity.setCreateTime(date);//创建时间
        activity.setCreateBy(user.getId());//创建人

        //执行
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityService.saveCreateActivity(activity);
            if(ret > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙,请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙,请稍后重试...");
        }

        return returnObject;
    }

    /**
     * 按条件查询市场活动
     * @param owner
     * @param name
     * @param startDate
     * @param endDate
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/workbench/activity/queryAllActivityByConditionForPage.do")
    @ResponseBody
    public Object queryAllActivityByConditionForPage(String name, String owner,
                                                     String startDate, String endDate,
                                    Integer pageNo,Integer pageSize){
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("owner",owner);
        map.put("name",name);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        //调用service
        //获取具体市场活动
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        //获取市场活动的个数
        int totalRows = activityService.queryCountByConditionForPage(map);

        //封装
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("activityList", activityList);//具体数据
        retMap.put("totalRows", totalRows);//总条数

        //响应信息
        return retMap;

    }

    /**
     * 根据id删除市场活动
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            int num = activityService.deleteActivityByIds(id);
            if(num>0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙,请稍后重试~~~");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙,请稍后重试~~~");
        }

        return returnObject;
    }

    /**
     * 根据id查询市场活动
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/selectActivityById.do")
    @ResponseBody
    public Object selectActivityById(String id) {
        Activity activity = activityService.selectActivityById(id);
       return activity;
    }

    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity, HttpSession session) {
        //再次封装参数
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        activity.setEditBy(user.getId());
        activity.setEditTime(DateUtils.formateDateTime(new Date()));

        ReturnObject returnObject = null;
        try {
            int i = activityService.saveEditActivity(activity);
            returnObject = new ReturnObject();
            //判断是否更新成功
            if(i == 1) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙,请稍后重试~");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙,请稍后重试~");
        }

        return returnObject;
    }


    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws IOException {
        //设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        //获取字节输出流
        ServletOutputStream outputStream = response.getOutputStream();

        //设置响应头信息,使浏览器接收到信息后,直接激活文件窗口,即使能打开也不打开
        response.addHeader("Content-Disposition", "attachment;filename=mystudent.xls");
        //读取excel文件(InputStream)
        InputStream is = new FileInputStream("D:\\SGG\\ProgramData\\crmTestDate\\studentList.xls");
        byte[] bytes = new byte[1024];
        int len = 0;
        while(-1 != (len = is.read(bytes))) {
            outputStream.write(bytes,0,len);
        }
        //关闭资源
        is.close();
        outputStream.close();
    }


    /**
     * 批量导出市场活动
     * @param response
     * @throws IOException
     */
    @RequestMapping("/workbench/activity/exportAllActivitys.do")
    public void exportAllActivitys(HttpServletResponse response) throws IOException {
        //查詢数据
        List<Activity> activityList = activityService.queryAllActivitys();

        //创建excel表格
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建页面
        HSSFSheet sheet = wb.createSheet("市场活动表");
        //创建行
        HSSFRow row = sheet.createRow(0);
        //创建列
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始时间");
        cell = row.createCell(4);
        cell.setCellValue("结束时间");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        //判断activityList是否有有效数据
        if(activityList != null && activityList.size()>0) {
            Activity activity = null;
            int count = activityList.size();
            //保存数据
            for (int i = 0; i<activityList.size(); i++) {
                //activity对象
                activity = activityList.get(i);
                //行
                row = sheet.createRow(i+1);
                //列
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

      /*  //根据wb对象生成excle文件  //已优化
        OutputStream os = new FileOutputStream("D:\\SGG\\ProgramData\\crmTestDate\\activitysList.xls");
        wb.write(os);
        //关闭资源
        os.close();
        wb.close();*/

        //设置响应头信息,使浏览器接收到信息后,直接激活文件窗口,即使能打开也不打开
        response.addHeader("Content-Disposition", "attachment;filename=mystudent.xls");
        //把生成的excle文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
       /* (已优化)InputStream is = new FileInputStream("D:\\SGG\\ProgramData\\crmTestDate\\activitysList.xls");
        byte[] buff = new byte[1024];
        int len;
        while((len=is.read(buff)) != -1) {
            out.write(buff,0,len);
        }*/
        //直接输出到浏览器
        wb.write(out);
        //关闭资源
        wb.close();
        out.flush();

    }

    /**
     * 选择批量导出市场活动
     * @param response
     * @throws IOException
     */
    @RequestMapping("/workbench/activity/exportActivitysById.do")
    public void exportActivitysById(String[] id, HttpServletResponse response) throws IOException {
        //查询数据
        List<Activity> activityList = activityService.queryActivitysById(id);

        //创建excel
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("客户表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始时间");
        cell = row.createCell(4);
        cell.setCellValue("结束时间");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        //判断activityList是否为空
        if(activityList != null && activityList.size()>0) {
            Activity activity = null;
            int count = activityList.size();
            //保存数据
            for (int i = 0; i<activityList.size(); i++) {
                //activity对象
                activity = activityList.get(i);
                //行
                row = sheet.createRow(i+1);
                //列
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }
        //设置响应信息
        response.addHeader("Content-Disposition","attachment;filename=mystudent.xls");
        response.setContentType("application/octet-stream;charset=UTF-8");

        //响应数据
        ServletOutputStream out = response.getOutputStream();
        wb.write(out);

        //关闭数据
        out.flush();
        out.close();
        wb.close();

    }

    //批量导入市场活动
    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile, HttpSession session) {
        String filename = null;
        HSSFWorkbook wb = null;
        ReturnObject ro = new ReturnObject();
        try {
           /*(优化) //把excel文件写到磁盘中
            //获取文件名
            filename = activityFile.getOriginalFilename();
            File file = new File("D:\\SGG\\ProgramData\\crmTestDate\\",filename);
            activityFile.transferTo(file);

            //解析excel文件,获取文件中的数据，并封装成Activity
            //创建HSSFWorkBook
            //InputStream inputStream = activityFile.getInputStream();
            FileInputStream is = new FileInputStream("D:\\SGG\\ProgramData\\crmTestDate\\"+filename);*/

            InputStream is = activityFile.getInputStream();
            wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = new Activity();
            activity.setId(UUIDUtils.getUUID());
            User user = (User) session.getAttribute(Constants.SESSION_USER);
            activity.setOwner(user.getId());
            activity.setCreateBy(user.getId());
            activity.setCreateTime(DateUtils.formatDateTime(new Date()));
            List<Activity> activityList = new ArrayList<>();
            for(int i = 1; i <= sheet.getLastRowNum(); i++) {
                //取出第i行数据
                row = sheet.getRow(i);
                //遍历行，并封装Activity
                for(int j = 0; j < row.getLastCellNum(); j++) {

                    cell = row.getCell(j);
                    //获取value
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if(j == 0) {
                        activity.setName(cellValue);
                    }else if(j == 1) {
                        activity.setStartDate(cellValue);
                    }else if(j == 2) {
                        activity.setEndDate(cellValue);
                    }else if(j == 3) {
                        activity.setCost(cellValue);
                    }else if(j == 4){
                        activity.setDescription(cellValue);
                    }

                }
                //把activity放到集合activityList里
                activityList.add(activity);
            }
            int num = activityService.saveCreateActivityByList(activityList);
            ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            ro.setMessage("保存成功");
            ro.setRetData(num);
        } catch (IOException e) {
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙,请稍后重试~");
        }
        return ro;
    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id, HttpServletRequest request) {
        //调用service层方法查询数据
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> activityRemarks = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        System.out.println("activityRemarks===================="+activityRemarks);

        //把数据保存到request作用域中
        request.setAttribute("activity", activity);
        request.setAttribute("activityRemarks", activityRemarks);

        //跳转页面
        return "workbench/activity/detail";

    };



}
