package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-20 11:25
 */
public interface ActivityRemarkService {
    /**
     * 根据市场活动id查询市场活动详细信息
     * @param activityId
     * @return
     */
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);

    /**
     * 插入市场活动的详细信息
     * @param activityRemark
     * @return
     */
    int saveCreateActivityRemark(ActivityRemark activityRemark);

    /**
     * 根据市场活动id删除指定市场活动
     * @param id
     * @return
     */
    int deleteActivityRemarkById(String id);

    /**
     * 保存修改市场活动
     * @param activityRemark
     * @return
     */
    int saveEditActivityRemarkById(ActivityRemark activityRemark);
}
