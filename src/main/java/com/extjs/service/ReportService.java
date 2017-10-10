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

    List reckonClassMark(String gradeno);

    void delRClassMark() throws SysException;

    /**
     * 班级成绩分布
     */
    List<RMarkArea> queryRMarkArea(String gradeno,String subjectno);

    /**
     * 错题统计
     */
    List<RWrongQuestion> queryRWrongQuestion();

    /**
     * 升学成绩模拟
     */
    List<RAboveSpecifiedMark> queryRAboveSpecifiedMark(String grade, String aboveMark, String dateArea);
    /**
     * 年度教学成绩统计
     */
    List<RYearMark> queryRYearMark(String year,String gradeno,String subjectno,String studentno,String studentname);
    /**
     * 学生年度成绩统计
     */
    List<RYearMarkStudent> queryRYearMarkStudent(String year,String studentname,String subjectno,String studentno);

}
