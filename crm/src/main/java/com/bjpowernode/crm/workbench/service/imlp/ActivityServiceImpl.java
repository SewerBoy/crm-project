package com.bjpowernode.crm.workbench.service.imlp;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-06 16:19
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 添加市场活动
     * @param activity
     * @return
     */
    @Override
    public int saveCreateActivity(Activity activity) {
        int i = activityMapper.insertActivity(activity);
        return i;
    }

    /**
     * 根据分页条件查询市场的活动列表
     * @param map
     * @return
     */
    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }
    /**
     * 根据分页条件查询市场活动的总条数
     * @param map
     * @return
     */
    @Override
    public int queryCountByConditionForPage(Map<String, Object> map) {
        return activityMapper.selectCountByConditionForPage(map);
    }

    /**
     * 根据id删除市场活动
     * @param ids
     * @return
     */
    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    /**
     * 根据id查询市场活动
     * @param id
     * @return
     */
    @Override
    public Activity selectActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    /**
     * 根据id修改市场活动
     * @param activity
     * @return
     */
    @Override
    public int saveEditActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    /**
     * 查询所有市场活动
     * @return
     */
    public List<Activity> queryAllActivitys() {
        return activityMapper.selectAllActivitys();
    }

    /**
     * 根据条件查询市场活动
     * @param ids
     * @return
     */
    public List<Activity> queryActivitysById(String[] ids) {
        return activityMapper.selectActivitysById(ids);
    }

    /**
     * 批量导入市场活动
     * @param activities
     * @return
     */
    @Override
    public int saveCreateActivityByList(List<Activity> activities) {
        return activityMapper.insertActivityByList(activities);
    }

    /**
     * 根基市场活动id查询市场活动的详细信息
     * @param id
     * @return
     */
    @Override
    public Activity queryActivityForDetailById(String id) {
        return activityMapper.selectActivityForDetailById(id);

    }

    /**
     * 根据线索id查询市场活动
     * @param id
     * @return
     */
    @Override
    public List<Activity> queryActivityForDetailByClueId(String id) {
        return activityMapper.selectActivityForDetailByClueId(id);
    }

    /**
     * 根据市场活动名称和id查询符合条件的市场活动
     * @param map
     * @return
     */
    @Override
    public List<Activity> queryActivityForDetailByNameClueId(Map map) {
        return activityMapper.selectActivityForDetailByNameClueId(map);
    }

    @Override
    public List<Activity> queryActivityBatchByIds(String[] ids) {
        return activityMapper.selectActivityBatchByIds(ids);
    }

    @Override
    public List<Activity> queryActivityForActivityNameClueId(Map map) {
        return activityMapper.selectActivityForActivityNameClueId(map);
    }

    @Override
    public List<Activity> queryActivityByVagueName(String name) {
        return activityMapper.selectActivityByVagueName(name);
    }
}
