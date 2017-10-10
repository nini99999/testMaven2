package com.extjs.dao;

import com.extjs.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jenny on 2017/4/9.
 */
public interface ReportDao {
    /**
     * 班级成绩统计表
     *
     * @param rClassMark
     * @return
     */
    List<RClassMark> queryRClassMark(RClassMark rClassMark);

    void addRClassMark(RClassMark rClassMark);

    void delRClassMark(String creator);

    /**
     * 班级成绩分布表
     */
    List<RMarkArea> queryRMarkArea(RMarkArea rMarkArea);

    void addRMarkArea(RMarkArea rMarkArea);

    void delRMarkArea(String creator);

    /**
     * 错题统计表
     */
    List<RWrongQuestion> queryRWrongQuestion(RWrongQuestion rWrongQuestion);

    void addRWrongQuestion(RWrongQuestion rWrongQuestion);

    void delRWrongQuestion(String creator);

    /**
     * 模拟分以上考生数量分布表
     */
    List<RAboveSpecifiedMark> queryRAboveSpecifiedMark(RAboveSpecifiedMark aboveSpecifiedMark);

    void addRAboveSpecifiedMark(RAboveSpecifiedMark aboveSpecifiedMark);

    void delRAboveSpecifiedMark(String creator);

    HashMap<String, Integer> getRAboveSpecifiedMark(RAboveSpecifiedMark aboveSpecifiedMark);

    /**
     * 年度教学成绩统计
     */
    List<RYearMark> queryRYearMark(RYearMark rYearMark);

    void addRYearMark(RYearMark rYearMark);

    void delRYearMark(String creator);

    /**
     * 年度学生成绩统计
     */
    List<RYearMarkStudent> queryRYearMarkStudent(RYearMarkStudent rYearMarkStudent);

    void addRYearMarkStudent(RYearMarkStudent rYearMarkStudent);

    void delRYearMarkStudent(String creator);
}
