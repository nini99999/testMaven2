package com.extjs.dao;

import com.extjs.model.EWrongStudent;
import com.extjs.model.EWrongStudentDTO;
import com.extjs.model.Page;
import com.extjs.model.RWrongQuestion;

import java.util.List;

/**
 * Created by jenny on 2017/5/22.
 */
public interface EwrongStudentDao {
    /**
     * 根据约束查询学生错题
     *
     * @param questionID
     * @param userName
     * @return
     */
    EWrongStudent getByUnique(String questionID, String studentID);

    List<EWrongStudent> queryEWrongStudent(EWrongStudentDTO wrongStudentDTO,Page page);

    /**
     * 根据日期区间查询错误信息
     *
     * @param beginDate
     * @param endDate
     * @param subjectno
     * @param gradeno
     * @return RWrongQuetionList
     */
    List<RWrongQuestion> getWrongsByDateArea(String beginDate, String endDate, String subjectno, String gradeno);

    void addEWrongStudent(EWrongStudent wrongStudent);

    void delEWrongStudent(EWrongStudent wrongStudent);

    void saveWrongAnalysis(EWrongStudent wrongStudent);

    int getTotalCount(EWrongStudentDTO wrongStudentDTO);
}
