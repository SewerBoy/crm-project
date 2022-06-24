package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-06 16:10
 */
public interface ActivityService {
    //添加市场活动
    int saveCreateActivity(Activity activity);

    //根据条件查询市场活动
    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

    //根据条件查询市场活动总条数
    int queryCountByConditionForPage(Map<String, Object> map);

    //根据id删除市场活动
    int deleteActivityByIds(String[] ids);

    //根据id查询市场活动
    Activity selectActivityById(String id);

    //根据id修改市场活动
    int saveEditActivity(Activity activity);

    //查询所有市场活动
    List<Activity> queryAllActivitys();

    //按条件查询市场活动
    List<Activity> queryActivitysById(String[] ids);

    //批量插入市场活动
    int saveCreateActivityByList(List<Activity> activities);

    //根基市场活动id查询市场活动的详细信息
    Activity queryActivityForDetailById(String id);

    //根基线索id查询市场活动
    List<Activity> queryActivityForDetailByClueId(String id);

    //根据市场活动名称和id查询符合条件的市场活动
    List<Activity> queryActivityForDetailByNameClueId(Map map);

    /**
     * 根据ids批量查询市场活动
     * @param ids
     * @return
     */
    List<Activity> queryActivityBatchByIds(String[] ids);

    /**
     * 根据市场活动名称和线索id模糊查询市场活动
     * @param map
     * @return
     */
    List<Activity> queryActivityForActivityNameClueId(Map map);

    /**
     * 根据名字模糊查询市场活动
     * @param name
     * @return
     */
    List<Activity> queryActivityByVagueName(String name);
}
