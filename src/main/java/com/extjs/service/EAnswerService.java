package com.extjs.service;

import com.extjs.model.EAnswer;
import com.extjs.util.SysException;

/**
 * Created by jenny on 2017/11/21.
 */
public interface EAnswerService {
    EAnswer getAnswer(String id);

    void saveAnswer(EAnswer eAnswer) throws SysException;

    void deleteAnswer(String id) throws SysException;
}
