package com.extjs.dao;

import com.extjs.model.EWrongStudent;
import com.extjs.model.EWrongStudentDTO;

import java.util.List;

/**
 * Created by jenny on 2017/5/22.
 */
public interface EwrongStudentDao {
    List<EWrongStudent> queryEWrongStudent(EWrongStudentDTO wrongStudentDTO);

    void addEWrongStudent(EWrongStudent wrongStudent);

    void delEWrongStudent(EWrongStudent wrongStudent);
}
