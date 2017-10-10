package com.extjs.dao;

import com.extjs.model.ETeacherClass;
import com.extjs.model.ETeacherClassDTO;

import java.util.List;

/**
 * Created by jenny on 2017/3/28.
 */
public interface EteacherClassDao {
    List<ETeacherClass> queryETeacherClass(ETeacherClassDTO eTeacherClassDTO);//查询教师-学科表
    String addETeacherClass(ETeacherClassDTO eTeacherClassDTO);
    void delETeacherClass(ETeacherClassDTO eTeacherClassDTO);
    void delETeacherClassByTeacherID(String teacherid);
}
