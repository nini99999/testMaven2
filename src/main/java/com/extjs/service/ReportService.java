package com.extjs.service;

import com.extjs.model.*;
import com.extjs.util.SysException;

import java.util.List;

/**
 * Created by jenny on 2017/4/9.
 */
public interface ReportService {
    /**
     * 班级平均成绩
     *
     * @param rClassMark
     * @return
     */
    List<RClassMark> queryRClassMark(RClassMark rClassMark);

    List reckonClassMark(RClassMark rClassMark);

    /**
     * 根据条件自动添加信息，条件包括：1、测试日期的开始和结束时间；2、gradeno班级编码；
     *
     * @param classMark
     * @throws SysException
     */
    void autoAddClassMark(RClassMark classMark) throws SysException;

    void addClassMark(RClassMark classMark) throws SysException;

    void delRClassMark() throws SysException;

    /**
     * 班级成绩分布
     */
    List<RMarkArea> queryRMarkArea(String gradeno, String tpno, String subjectno);

    void delMarkArea(String creator) throws SysException;

    void addMarkArea(RMarkArea markArea) throws SysException;

    /**
     * 自动计算符合条件的学生成绩分布(指定试卷和年级)，并写入至表中
     *
     * @param tpno
     * @param gradeno
     * @throws SysException
     */
    void autoAddMarkAreaBySubject(String tpno, String gradeno) throws SysException;

    /**
     * 自动计算符合条件的学生成绩分布(指定试卷列表和年级)，并写入至表中
     *
     * @param gradeno
     * @param tpnoString
     * @throws SysException
     */
    void autoAddMarkAreaTotal(String gradeno, String tpnoString) throws SysException;

    /**
     * 错题统计
     */
    List<RWrongQuestion> queryRWrongQuestion(String beginDate, String endDate, String subjectno, String gradeno, String classno);

    /**
     * 升学成绩模拟
     */
    List<RAboveSpecifiedMark> queryRAboveSpecifiedMark(String grade, String aboveMark, String tpnoString);

    /**
     * 年度教学成绩统计
     */
    List<String[]> queryRYearMark(String year, String gradeno, String subjectno, String studentno, String studentname);

    /**
     * 学生年度成绩统计
     */
    List<RYearMarkStudent> queryRYearMarkStudent(String year, String studentname, String subjectno, String studentno);

    /**
     * 学生年度成绩统计
     *
     * @param year
     * @param studentID
     * @return
     */
    List<String[]> getYearMarkStudent(String year, String studentID);

    /**
     * 根据指定学生所在班级，查询指定年度的平均成绩（月考、期中、期末），按学科排序
     *
     * @param studentID
     * @param year
     * @return
     */
    Float[] getAvgMarkByClass(String studentID, String year);

    /**
     * 根据指定学生所在年级，查询指定年度的平均成绩（月考、期中、期末），按学科排序
     *
     * @param studentID
     * @param year
     * @return
     */
    Float[] getAvgMarkByGrade(String studentID, String year);

}
