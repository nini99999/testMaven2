package com.extjs.dao;

import com.extjs.model.EGrade;
import com.extjs.model.EGradeDTO;


import java.util.List;

/**
 * Created by jenny on 2017/3/20.
 */
public interface EgradeDao {
    EGrade queryEgradeByID(String gradeno);//根据编码查询
    List<EGrade> queryEgrade();//查询所有年级
    List<EGrade> queryEgradeByschoolno(String schoolno);//根据学校查询所有年级
    String addEgrade(EGradeDTO eGradeDTO) ;//添加年级
    void delEgrade(EGradeDTO eGradeDTO);//删除年级
}
