package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.TransactionRemark;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-10 15:34
 */
public interface TransactionRemarkService {
    //根据交易id查询备注
    List<TransactionRemark> queryTransactionRemarkByTranId(String tranId);
}
