package com.extjs.dao;

import com.extjs.model.EAnswer;

/**
 * Created by jenny on 2017/11/21.
 */
public interface EAnswerDao {
    EAnswer getAnswer(String id);//根据questionid获取答案
    void saveAnswer(EAnswer answer);
    void delAnswer(String id);

}
