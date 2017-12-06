package com.extjs.service;

import com.extjs.model.EStudentMark;
import com.extjs.model.Page;

import java.util.List;

/**
 * Created by jenny on 2017/5/12.
 */
public interface EstudentMarkService {
    List<EStudentMark> queryEStudentMark(EStudentMark eStudentMark, Page page);

    /**
     * 根据时间区域和年级查询班级平均成绩,按照学科、试卷和班级分组
     *
     * @param eStudentMark
     * @param gradeno
     * @return
     */
    List<EStudentMark> getAverageMark(EStudentMark eStudentMark, String gradeno);

    /**
     * markArea的组装方式like："0，79"，根据试卷、班级查询指定成绩区间内的学生成绩数量
     *
     * @param tpno
     * @param classno
     * @param markArea
     * @return
     */
    int getMarkAreaNum(String tpno, String classno, String markArea);

    /**
     * 根据班级、试卷考试日期（月）、学科统计平均分
     *
     * @param classno
     * @param testDate  格式应为："201706"的月份
     * @param subjectno
     * @return
     */
    Float getAvgMark(String classno, String testDate, String subjectno);

    /**
     * 获取期中或期末考试成绩平均分
     * @param classno
     * @param year
     * @param subjectno
     * @param examType
     * @return
     */
    Float getAvgMiddleOrFinal(String classno, String year, String subjectno, String examType);

    int getMareAreaTotalNum(String classno, String markArea, String tpnoString);

    void modifEStudentMark(EStudentMark eStudentMark);

    void modifOnlyMark(EStudentMark studentMark);

    void delEStudentMark(EStudentMark eStudentMark);

    void delEStudentMark();

    int getTotalCount(EStudentMark eStudentMark);
}
