package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.TransactionHistory;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-10 15:42
 */
public interface TransactionHistoryService {
    //根据交易id查询交易记录
    List<TransactionHistory> queryTransactionHistoryByTranId(String tranId);
}
