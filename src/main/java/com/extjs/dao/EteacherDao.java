package com.extjs.dao;

import com.extjs.model.ETeacher;
import com.extjs.model.ETeacherDTO;
import com.extjs.util.SysException;

import java.util.List;

/**
 * Created by jenny on 2017/3/26.
 */
public interface EteacherDao {
    List<ETeacher> queryEteacher(ETeacherDTO eTeacherDTO);//查询教师

    String addEteacher(ETeacherDTO eTeacherDTO) throws SysException;//添加教师

    void delEteacher(ETeacherDTO eTeacherDTO) throws SysException;//删除教师
}
