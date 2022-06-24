package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-26 15:46
 */
@Service
public interface DicValueService {
    /**
     * 根据类型查询DicValue
     * @param typeCode
     * @return
     */
    public List<DicValue> queryDicValueByTypeCode(String typeCode);
}
