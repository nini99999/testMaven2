package com.extjs.dao;

import com.extjs.model.EWrongStudent;
import com.extjs.model.EWrongStudentDTO;
import com.extjs.model.RWrongQuestion;

import java.util.List;

/**
 * Created by jenny on 2017/5/22.
 */
public interface EwrongStudentDao {
    List<EWrongStudent> queryEWrongStudent(EWrongStudentDTO wrongStudentDTO);

    /**
     * 根据日期区间查询错误信息
     * @param beginDate
     * @param endDate
     * @param subjectno
     * @param gradeno
     * @return RWrongQuetionList
     */
    List<RWrongQuestion> getWrongsByDateArea(String beginDate,String endDate,String subjectno,String gradeno);

    void addEWrongStudent(EWrongStudent wrongStudent);

    void delEWrongStudent(EWrongStudent wrongStudent);
}
