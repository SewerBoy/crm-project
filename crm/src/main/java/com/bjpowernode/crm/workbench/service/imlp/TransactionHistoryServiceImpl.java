package com.bjpowernode.crm.workbench.service.imlp;

import com.bjpowernode.crm.workbench.domain.TransactionHistory;
import com.bjpowernode.crm.workbench.mapper.TransactionHistoryMapper;
import com.bjpowernode.crm.workbench.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-10 15:45
 */
@Service("transactionHistoryServiceImpl")
public class TransactionHistoryServiceImpl implements TransactionHistoryService {
    @Autowired
    private TransactionHistoryMapper thm;
    @Override
    public List<TransactionHistory> queryTransactionHistoryByTranId(String tranId) {
        return thm.selectTransactionHistoryByTranId(tranId);
    }
}
