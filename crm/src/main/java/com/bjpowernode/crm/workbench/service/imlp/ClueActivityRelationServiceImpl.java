package com.bjpowernode.crm.workbench.service.imlp;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.mapper.ClueActivityRelationMapper;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccstart
 * @Description
 * @create 2022-06-04 15:52
 */
@Service("clueActivityRelationServiceImpl")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Override
    public int saveClueActivityRelation(List<ClueActivityRelation> clueActivityRelationList) {
        return clueActivityRelationMapper.insertClueActivityRelationBatch(clueActivityRelationList);
    }

    @Override
    public int deleteClueActivityRelation(ClueActivityRelation car) {
        return clueActivityRelationMapper.deleteClueActivityRelation(car);
    }
}
