package com.extjs.service;

/**
 * Created by jenny on 2017/3/16.
 */
import com.extjs.model.EsubjectsDTO;
import com.extjs.util.SysException;

import java.util.HashMap;
import java.util.List;

public interface EsubjectsService {
    List<EsubjectsDTO> queryEsubjectsList();//查询学科，返回列表
    HashMap<String ,String> queryEsubjectMap();//查询学科，返回map
    void addEsubjects(EsubjectsDTO esubjectsDTO) throws SysException;//添加学科
    void delEsubjects(EsubjectsDTO esubjectsDTO) throws SysException;//删除学科
}
