package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.domain.Transaction;

import java.util.List;
import java.util.Map;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-09 21:44
 */
public interface TransactionService {
    /**
     * 创建一条交易
     * @param map
     * @return
     */
    void saveTransaction(Map<String, Object> map);

    //查询全部交易
    List<Transaction> selectAllTransaction();

    //根据id查询交易
    Transaction queryTransactionForDetailById(String id);

    //根据stage分组查询个个总条数
    List<FunnelVO> queryCountOfGroupByStage();
}
