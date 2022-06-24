package com.bjpowernode.crm.workbench.service.imlp;

import com.bjpowernode.crm.commons.constants.Constants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-27 9:49
 */
@Service("clueServiceImpl")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;

    @Override
    public int saveClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryClueByCondition(Map map) {
        return clueMapper.selectClueByCondition(map);
    }

    @Override
    public int queryClueCountByCondition(Map map) {
        return clueMapper.selectClueCountByCondition(map);
    }
    //根据id查询线索
    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);

    }

    /*@Override
    public Clue queryClueForId(String id) {
        return clueMapper.selectClueForId(id);
    }*/

    @Override
    public void saveConvert(Map<String, Object> map) {
        //封装数据
        User user = (User) map.get(Constants.SESSION_USER);
        String clueId = (String) map.get("clueId");
        Clue clue = clueMapper.selectClueForId(clueId);
        Customer customer = new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setOwner(user.getId());
        customer.setName(clue.getFullname());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(clue.getCreateBy());
        customer.setCreateTime(clue.getCreateTime());
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setDescription(clue.getDescription());
        customer.setAddress(clue.getAddress());
        //调用service层,数据保存到客户表中
        customerMapper.insertCustomer(customer);

        //封装数据
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtils.getUUID());
        contacts.setOwner(user.getId());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(clue.getCreateTime());
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        //调用service层,保存到联系人表中
        contactsMapper.insertContacts(contacts);

        //根据ClueId查询备注
        List<ClueRemark> clueRemarks = clueRemarkMapper.selectClueRemarkByClueId(clueId);

        //把线索备注转换到联系人备注表中,且转换到客户备注表中
        if(clueRemarks!=null && clueRemarks.size()!=0) {
            ContactsRemark cr = null;
            CustomerRemark cu = null;
            List<ContactsRemark> contactsRemarkList = new ArrayList<>();
            List<CustomerRemark> customerRemarkList = new ArrayList<>();
            for(int i = 0 ; i < clueRemarks.size(); i++) {
                //联系人备注
                cr = new ContactsRemark();
                cr.setId(UUIDUtils.getUUID());
                cr.setNoteContent(clueRemarks.get(i).getNoteContent());
                cr.setContactsId(customer.getId());
                cr.setCreateBy(clueRemarks.get(i).getCreateBy());
                cr.setCreateTime(clueRemarks.get(i).getCreateTime());
                cr.setEditBy(clueRemarks.get(i).getEditBy());
                cr.setEditFlag(clueRemarks.get(i).getEditFlag());
                cr.setEditTime(clueRemarks.get(i).getEditTime());
                contactsRemarkList.add(cr);

                //客户备注
                cu = new CustomerRemark();
                cu.setId(UUIDUtils.getUUID());
                cu.setNoteContent(clueRemarks.get(i).getNoteContent());
                cu.setCreateBy(clueRemarks.get(i).getCreateBy());
                cu.setCreateTime(clueRemarks.get(i).getCreateTime());
                cu.setEditBy(clueRemarks.get(i).getEditBy());
                cu.setEditTime(clueRemarks.get(i).getEditTime());
                cu.setEditFlag(clueRemarks.get(i).getEditFlag());
                cu.setCustomerId(customer.getId());
                customerRemarkList.add(cu);
            }
            contactsRemarkMapper.insertContactsRemarkByList(contactsRemarkList);
            customerRemarkMapper.insertCustomerRemark(customerRemarkList);
        }

        //根据ClueId查询线索市场活动关系
        List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);
        if(carList!=null && carList.size()>0) {
            ContactsActivityRelation conar = null;
            List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
            for(ClueActivityRelation car : carList) {
                conar = new ContactsActivityRelation();
                conar.setId(UUIDUtils.getUUID());
                conar.setActivityId(car.getActivityId());
                conar.setContactsId(contacts.getId());
                contactsActivityRelationList.add(conar);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationByList(contactsActivityRelationList);
        }

        //是否创建交易，和交易备注表
        String isCreateTran = (String)map.get("isCreateTran");
        if("true".equals(isCreateTran)) {
            Transaction tran = new Transaction();
            tran.setId(UUIDUtils.getUUID());
            tran.setOwner(user.getId());
            tran.setName((String)map.get("name"));
            tran.setExpectedDate((String)map.get("expectedDate"));
            tran.setCustomerId(customer.getId());
            tran.setStage((String)map.get("stage"));
            tran.setSource((String)map.get("source"));
            tran.setActivityId((String)map.get("activityId"));
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtils.formatDateTime(new Date()));
            //调用service层保存数据
            transactionMapper.insertTransaction(tran);

            //交易备注表
            //把线索备注转换到交易备注表中
            if(clueRemarks!=null && clueRemarks.size()!=0) {
                TransactionRemark transactionRemark = null;
                List<TransactionRemark> trs = new ArrayList<>();
                for(ClueRemark cr : clueRemarks) {
                    transactionRemark =  new TransactionRemark();
                    transactionRemark.setId(UUIDUtils.getUUID());
                    transactionRemark.setNoteContent(cr.getNoteContent());
                    transactionRemark.setCreateBy(cr.getCreateBy());
                    transactionRemark.setCreateTime(cr.getCreateTime());
                    transactionRemark.setEditBy(cr.getEditBy());
                    transactionRemark.setEditTime(cr.getEditTime());
                    transactionRemark.setEditFlag(cr.getEditFlag());
                    transactionRemark.setTranId(tran.getId());
                    //放入数组
                    trs.add(transactionRemark);
                }
                //调用service层保存数据
                transactionRemarkMapper.insertTransactionRemark(trs);

            }
        };

        //根据线索id删除全部线索备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);

        //删除该线索和市场活动的关联关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueIdActivityId(clueId);

        //删除该线索
        clueMapper.deleteClueById(clueId);

    }
}
