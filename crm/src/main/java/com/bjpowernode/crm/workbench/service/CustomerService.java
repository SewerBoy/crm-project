package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-06 12:00
 */
public interface CustomerService {
    /**
     * 保存用户
     * @param customer
     */
    int saveCustomer(Customer customer);

    /**
     * 根据名称查询客户
     * @param name
     * @return
     */
    List<Customer> queryCustomerByName(String name);

    /**
     * 根据名称精确查询客户
     * @param name
     * @return
     */
    Customer queryCustomerByPreciseName(String name);

}
