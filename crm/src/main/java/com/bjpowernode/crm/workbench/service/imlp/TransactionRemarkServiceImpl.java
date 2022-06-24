package com.bjpowernode.crm.workbench.service.imlp;

import com.bjpowernode.crm.workbench.domain.TransactionRemark;
import com.bjpowernode.crm.workbench.mapper.TransactionRemarkMapper;
import com.bjpowernode.crm.workbench.service.TransactionRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-10 15:35
 */
@Service("transactionRemarkServiceImpl")
public class TransactionRemarkServiceImpl implements TransactionRemarkService {
    @Autowired
    private TransactionRemarkMapper trm;

    @Override
    public List<TransactionRemark> queryTransactionRemarkByTranId(String tranId) {
        return trm.selectTransactionRemarkByTransactionId(tranId);
    }
}
