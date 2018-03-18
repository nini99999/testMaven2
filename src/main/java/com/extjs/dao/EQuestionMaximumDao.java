package com.extjs.dao;

import com.extjs.model.EQuestionMaximum;
import com.extjs.util.SysException;

import java.util.List;

public interface EQuestionMaximumDao {
    EQuestionMaximum getQuestionMaximum(String id);//根据试题id查询分值

    List<EQuestionMaximum> getQuestionMaximum(EQuestionMaximum questionMaximum);//根据试卷id和题号查询

    Float getSumMaximum(String paperId, String questionTypeId);//获取指定试卷、题型的总分

    void addQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException;

    void updateQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException;

    void modifQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException;

    void delQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException;

}
