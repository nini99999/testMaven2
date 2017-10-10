package com.extjs.service;

import com.extjs.model.ESchoolDTO;
import com.extjs.util.SysException;
import java.util.List;

/**
 * Created by jenny on 2017/3/18.
 */
public interface EschoolService {
    List<ESchoolDTO> queryEschool();//查询所有学校
    ESchoolDTO querySchoolByUnique(String id,String schoolno);//根据id或学校编码查询
    void addEschool(ESchoolDTO eSchoolDTO) throws SysException;//添加学校
    void delEschool(ESchoolDTO eSchoolDTO) throws SysException;//删除学校
}
