package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-03 17:09
 */
public interface ClueRemarkService {
    /**
     * 根据线索id查询备注信息
     * @param id
     * @return
     */
    List<ClueRemark> queryClueRemarkForDetailByClueId(String id);

    /**
     * 根据clueId查询备注信息
     * @param clueId
     * @return
     */
    List<ClueRemark> queryClueRemarkByClueId(String clueId);

    /**
     * 保存线索备注
     * @param clueRemark
     * @return
     */
    int saveClueRemark(ClueRemark clueRemark);

}
