package com.extjs.dao;

import com.extjs.model.ETeacherClass;
import com.extjs.model.ETeacherClassDTO;
import com.extjs.model.VTeacherClass;

import java.util.List;

/**
 * Created by jenny on 2017/3/28.
 */
public interface EteacherClassDao {
    List<ETeacherClass> queryETeacherClass(ETeacherClassDTO eTeacherClassDTO);//查询教师-学科表

    /**
     * 查询教师—授课班级视图,
     * @param teacherClass 查询条件：班级+学科
     * @return 如存在多条记录，说明同一班级、学科的授课老师有多个，以,分隔
     */
   VTeacherClass getTeacherClass(VTeacherClass teacherClass);

    String addETeacherClass(ETeacherClassDTO eTeacherClassDTO);

    void delETeacherClass(ETeacherClassDTO eTeacherClassDTO);

    void delETeacherClassByTeacherID(String teacherid);
}
