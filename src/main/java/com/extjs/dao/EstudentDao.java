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

    List<EStudent> queryEstudentByClassAndTpno(String classno,String tpno);//查询指定条件下，未包括在学生成绩表中的学生

    EStudent getEstudentByCountryID(String countryID);

    EStudent getStudentByID(String id);

    String addEstudent(EStudentDTO eStudentDTO);//添加学生

    void delEstudent(EStudentDTO eStudentDTO);//删除学生
}
