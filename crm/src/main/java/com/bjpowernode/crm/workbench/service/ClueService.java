package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-27 9:47
 */
public interface ClueService {

    /**
     * 保存线索
     * @return
     */
    public int saveClue(Clue clue);

    /**
     * 查询线索
     * @param map
     * @return
     */
    public List<Clue> queryClueByCondition(Map map);

    /**
     * 查询总条数
     * @param map
     * @return
     */
    int queryClueCountByCondition(Map map);

    /**
     * 根据id查询线索
     * @param id
     * @return
     */
    Clue queryClueById(String id);

   /**
     * 根据id查询线索(无多表连接，转换的时候用的)
     * @param
     * @return
     *//*
    Clue queryClueForId(String id);*/

    /**
     *
     */
    void saveConvert(Map<String, Object> map);
}
