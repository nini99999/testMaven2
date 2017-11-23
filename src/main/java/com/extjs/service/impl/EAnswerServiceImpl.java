package com.extjs.service.impl;

import com.extjs.dao.EAnswerDao;
import com.extjs.model.EAnswer;
import com.extjs.service.EAnswerService;
import com.extjs.util.SysException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by jenny on 2017/11/21.
 */
@Service
@Scope("prototype")
public class EAnswerServiceImpl implements EAnswerService {
    @Autowired
    private EAnswerDao eAnswerDao;

    @Override
    public EAnswer getAnswer(String id) {
        EAnswer answer = eAnswerDao.getAnswer(id);
        if (null != answer) {
            if (null != answer.getAnswer() && answer.getAnswer().length() > 0) {
                answer.setAnswer(
                        StringEscapeUtils.unescapeXml(answer.getAnswer()));
            }
        }
        return answer;
    }

    @Override
    public void saveAnswer(EAnswer eAnswer) throws SysException {
        eAnswerDao.saveAnswer(eAnswer);
    }

    @Override
    public void deleteAnswer(String id) throws SysException {
        eAnswerDao.delAnswer(id);
    }
}
