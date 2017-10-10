package com.extjs.dao;

import com.extjs.model.ESubjectQType;
import com.extjs.model.ESubjectQTypeDTO;
import com.extjs.util.SysException;

import java.util.List;

/**
 * Created by jenny on 2017/4/2.
 */
public interface ESubjectQTypeDao {
    List<ESubjectQType> queryESubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO);//根据DTO查询学科题型表

    String addESubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO) throws SysException;//保存学科题型表

    void delESubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO) throws SysException;//删除

    void delESubjectQTByID(String id) throws SysException;//根据id删除；
}
