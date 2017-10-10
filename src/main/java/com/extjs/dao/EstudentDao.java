package com.extjs.dao;

import com.extjs.model.EStudentDTO;
import com.extjs.model.EStudent;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jenny on 2017/3/24.
 */
public interface EstudentDao {
    List<EStudent> queryEstudent(EStudentDTO eStudentDTO);//根据条件查询

    EStudent getEstudentByCountryID(String countryID);

    String addEstudent(EStudentDTO eStudentDTO);//添加学生

    void delEstudent(EStudentDTO eStudentDTO);//删除学生
}
