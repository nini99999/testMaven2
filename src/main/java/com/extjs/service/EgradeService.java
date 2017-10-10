package com.extjs.service;

import com.extjs.model.EGrade;
import com.extjs.model.EGradeDTO;
import com.extjs.util.SysException;

import java.util.List;

/**
 * Created by jenny on 2017/3/20.
 */
public interface EgradeService {
    List<EGradeDTO> queryEgrade();//查询所有年级
    List<EGradeDTO> queryEgradeByschoolno(String schoolno);//根据schoolno查询年级
    void addEgrade(EGradeDTO eGradeDTO) throws SysException;//添加年级
    void delEgrade(EGradeDTO eGradeDTO) throws SysException;//删除年级
}
