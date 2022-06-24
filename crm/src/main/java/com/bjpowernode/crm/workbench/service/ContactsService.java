package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Contacts;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-09 11:27
 */
public interface ContactsService {
    /**
     * 通过名字模糊查询联系人
     * @param fullname
     * @return
     */
    List<Contacts> queryContactsByVagueName(String fullname);
}
