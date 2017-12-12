package com.extjs.dao;

import com.extjs.model.EStudentMark;
import com.extjs.model.Page;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by jenny on 2017/5/11.
 */
public interface EStudentMarkDao {


    List<EStudentMark> queryEStudentMark(EStudentMark eStudentMark, Page page);

    /**
     * 获取指定年级、考试日期区间的平均成绩
     *
     * @param studentMark
     * @param gradeno
     * @return
     */
    List<EStudentMark> getAverageMark(EStudentMark studentMark, String gradeno);

    /**
     * 根据学生姓名、考试月份，查询该月的平均成绩（月考）
     *
     * @param studentID
     * @param testDate  格式为"201707"
     * @return
     */
    Float getAvgMarkByStudent(String studentID, String testDate, String subjectno);

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
     * 根据班级和年度查询各学科的平均分（月考、期中、期末）
     *
     * @param classno
     * @param year
     * @return
     */
    LinkedHashMap<String, Float> getAvgMarkByClass(String classno, String year);

    /**
     * 根据年级和年度查询各学科的平均分（月考、期中、期末）
     *
     * @param gradeno
     * @param year
     * @return
     */
    LinkedHashMap<String, Float> getAvgMarkByGrade(String gradeno, String year);

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
     * 期中或期末平均分
     *
     * @param studentID 学生ID
     * @param year
     * @param subjectno
     * @return
     */
    Float getAvegMiddleOrFinalByStudent(String studentID, String year, String subjectno, String examType);

    /**
     * 考生数，按试卷进行分组
     *
     * @return
     */
    HashMap<String, Integer> getStudentNum();

    int getTotalCount(EStudentMark eStudentMark);

    void saveOrUpdateEStudentMark(EStudentMark eStudentMark);

    void modifOnlyMark(EStudentMark studentMark);//仅更新成绩

    void delEStudentMark(EStudentMark eStudentMark);

    void delEStudentMarkByCreator(String creator);


}
