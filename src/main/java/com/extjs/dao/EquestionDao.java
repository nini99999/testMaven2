package com.extjs.dao;

import com.extjs.model.EQuestions;

import java.util.List;

/**
 * Created by jenny on 2017/6/20.
 */
public interface EquestionDao {
    List<EQuestions> getQuestions(EQuestions eQuestions);//查询试题记录

    String addQuestion(EQuestions eQuestions);//添加一条试题记录

    void delQuestion(String questionID);//根据id删除试题

    EQuestions updateQuestion(EQuestions eQuestions);//根据po修改一条试题记录


}
