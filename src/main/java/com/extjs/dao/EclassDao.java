package com.extjs.dao;

import com.extjs.model.EClass;
import com.extjs.model.EClassDTO;

import java.util.List;

/**
 * Created by jenny on 2017/3/22.
 */
public interface EclassDao {
    List<EClass> queryEclass();//查询所有班级

    List<EClass> queryEclass(EClassDTO eClassDTO);//根据条件查询

    String addEclass(EClassDTO eClassDTO);

    void delEclass(EClassDTO eClassDTO);//根据条件删除
}
