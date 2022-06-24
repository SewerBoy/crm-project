package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-04 15:51
 */
public interface ClueActivityRelationService {

    /**
     * 批量保存ClueActivityRelation
     * @param clueActivityRelationList
     * @return
     */
    int saveClueActivityRelation(List<ClueActivityRelation> clueActivityRelationList);

    /**
     * 删除线索市场活动关系
     * @param car
     * @return
     */
    int deleteClueActivityRelation(ClueActivityRelation car);
}
