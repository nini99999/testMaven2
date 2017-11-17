package com.extjs.dao;

import com.extjs.model.EStudentMark;
import com.extjs.model.Page;

import java.util.List;

/**
 * Created by jenny on 2017/5/11.
 */
public interface EStudentMarkDao {


    List<EStudentMark> queryEStudentMark(EStudentMark eStudentMark, Page page);

    int getTotalCount(EStudentMark eStudentMark);

    void saveOrUpdateEStudentMark(EStudentMark eStudentMark);

    void modifOnlyMark(EStudentMark studentMark);//仅更新成绩

    void delEStudentMark(EStudentMark eStudentMark);

    void delEStudentMarkByCreator(String creator);


}
