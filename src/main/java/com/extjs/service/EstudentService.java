package com.extjs.service;

import com.extjs.dao.EstudentDao;
import com.extjs.model.EStudentDTO;
import com.extjs.util.SysException;

import java.util.List;

/**
 * Created by jenny on 2017/3/24.
 */
public interface EstudentService {
    List<EStudentDTO> queryEstudent(EStudentDTO eStudentDTO);//根据条件查询
    String addEstudent(EStudentDTO eStudentDTO) throws SysException;//添加
    void delEstudent(EStudentDTO eStudentDTO) throws SysException;//删除
}
