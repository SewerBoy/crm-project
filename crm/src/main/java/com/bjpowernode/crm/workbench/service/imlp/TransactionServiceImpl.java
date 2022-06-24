package com.bjpowernode.crm.workbench.service.imlp;

import com.bjpowernode.crm.commons.constants.Constants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.domain.Transaction;
import com.bjpowernode.crm.workbench.domain.TransactionHistory;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TransactionHistoryMapper;
import com.bjpowernode.crm.workbench.mapper.TransactionMapper;
import com.bjpowernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-09 21:45
 */
@Service("transactionServiceImpl")
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionHistoryMapper thm;

    @Override
    public List<Transaction> selectAllTransaction() {
        return transactionMapper.selectAllTransaction();
    }

    @Override
    public void saveTransaction(Map<String, Object> map) {
        User user = (User)map.get(Constants.SESSION_USER);
        //精确查询是否存在该用具,如果不存在则创建
        Customer customer = customerMapper.selectCustomerByPreciseName((String) map.get("customerName"));
        if(customer==null) {
            //创建用户
            customer = new Customer();
            customer.setName((String)map.get("name"));
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DateUtils.formatDateTime(new Date()));
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setName((String)map.get("customerName"));
            //调用service层保存数据
            customerMapper.insertCustomer(customer);
        }

        //保存交易
        Transaction tran = new Transaction();
        tran.setId(UUIDUtils.getUUID());
        tran.setName((String)map.get("name"));
        tran.setOwner((String)map.get("owner"));
        tran.setMoney((String)map.get("money"));
        tran.setExpectedDate((String)map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setStage((String)map.get("stage"));
        tran.setType((String)map.get("type"));
        tran.setSource((String)map.get("source"));
        tran.setActivityId((String)map.get("activityId"));
        tran.setContactsId((String)map.get("contactsId"));
        tran.setCreateTime(DateUtils.formatDateTime(new Date()));
        tran.setCreateBy(user.getId());
        tran.setDescription((String)map.get("description"));
        tran.setContactSummary((String)map.get("contactSummary"));
        tran.setNextContactTime((String)map.get("nextContactTime"));
        //調用service层保存数据
        transactionMapper.insertTransaction(tran);

        //添加一条交易历史
        TransactionHistory th = new TransactionHistory();
        th.setId(UUIDUtils.getUUID());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setExpectedDate(tran.getExpectedDate());
        th.setCreateBy(user.getId());
        th.setCreateTime(DateUtils.formatDateTime(new Date()));
        th.setTranId(tran.getId());
        //调用sercice层保存数据
        thm.insertTransactionHistory(th);

    }

    @Override
    public Transaction queryTransactionForDetailById(String id) {
        return transactionMapper.selectTransactionForDetailById(id);
    }

    @Override
    public List<FunnelVO> queryCountOfGroupByStage() {
        return transactionMapper.selectCountOfTranGroupByStage();
    }
}
