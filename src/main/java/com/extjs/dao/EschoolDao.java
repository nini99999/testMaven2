package com.extjs.dao;

import java.util.List;
import com.extjs.model.ESchool;
import com.extjs.model.ESchoolDTO;

/**
 * Created by jenny on 2017/3/18.
 */
public interface EschoolDao {
    List<ESchool> queryEschool();//查询所有学校
    String addEschool(ESchoolDTO eSchoolDTO);//增加学校
    void delEschool(ESchoolDTO eSchoolDTO);//删除学校
    ESchool queryEschool(ESchool eSchool);
}
