package com.extjs.dao;


import com.extjs.model.Esubjects;
import com.extjs.model.EsubjectsDTO;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jenny on 2017/3/16.
 */
public interface EsubjectsDao {

    List<Esubjects> queryEsubjectsList();//查询所有学科


    String addEsubjects(EsubjectsDTO esubjectsDTO);//添加学科

    //List<Esubjects> getEsubjectsList(String grade);//根据年级查询学科
    void delEsbujects(EsubjectsDTO esubjectsDTO);
}
