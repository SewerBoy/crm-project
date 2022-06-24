package com.bjpowernode.crm.workbench.service.imlp;

import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.mapper.ContactsMapper;
import com.bjpowernode.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-09 11:29
 */
@Service("contactsServiceImpl")
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsMapper contactsMapper;
    @Override
    public List<Contacts> queryContactsByVagueName(String fullname) {
        return contactsMapper.selectContactsByVagueName(fullname);
    }
}
