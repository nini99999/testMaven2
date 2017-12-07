package com.extjs.dao;

import com.extjs.model.EStudentMark;
import com.extjs.model.Page;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jenny on 2017/5/11.
 */
public interface EStudentMarkDao {


    List<EStudentMark> queryEStudentMark(EStudentMark eStudentMark, Page page);

    /**
     * 获取指定年级、考试日期的平均成绩
     *
     * @param studentMark
     * @param gradeno
     * @return
     */
    List<EStudentMark> getAverageMark(EStudentMark studentMark, String gradeno);

    /**
     * 根据试卷编码，班级获取指定成绩区间内的学生成绩人数
     *
     * @param tpno
     * @param classno
     * @param markArea
     * @return
     */
    int getMarkAreaNum(String tpno, String classno, String markArea);

    /**
     * 根据班级、成绩区间、试卷编码组成的字符串（格式要求："tpno1,tpno2,tpno3..."）
     *
     * @param classno
     * @param markArea
     * @param tpnoString
     * @return
     */
    int getMarkAreaTotalNum(String classno, String markArea, String tpnoString);

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
     * 期中或期末平均分
     *
     * @param classno
     * @param year
     * @param subjectno
     * @return
     */
    Float getAvgMiddleOrFinal(String classno, String year, String subjectno, String examType);

    /**
     * query考生数，按试卷进行分组
     * @return
     */
    HashMap<String, Integer> getStudentNum();

    int getTotalCount(EStudentMark eStudentMark);

    void saveOrUpdateEStudentMark(EStudentMark eStudentMark);

    void modifOnlyMark(EStudentMark studentMark);//仅更新成绩

    void delEStudentMark(EStudentMark eStudentMark);

    void delEStudentMarkByCreator(String creator);


}
