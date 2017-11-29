package com.extjs.service;

import com.extjs.model.EStudentMark;
import com.extjs.model.Page;

import java.util.List;

/**
 * Created by jenny on 2017/5/12.
 */
public interface EstudentMarkService {
    List<EStudentMark> queryEStudentMark(EStudentMark eStudentMark,Page page);

    List<EStudentMark> getAverageMark(EStudentMark eStudentMark,String gradeno);//根据时间区域和年级查询班级平均成绩,按照学科、试卷和班级分组

    void modifEStudentMark(EStudentMark eStudentMark);

    void modifOnlyMark(EStudentMark studentMark);

    void delEStudentMark(EStudentMark eStudentMark);

    void delEStudentMark();
    int getTotalCount(EStudentMark eStudentMark);
}
