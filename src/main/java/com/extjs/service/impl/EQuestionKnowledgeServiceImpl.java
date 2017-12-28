package com.extjs.service.impl;

import com.extjs.dao.EQuestionKnowledgeDao;
import com.extjs.model.EKnowledge;
import com.extjs.model.EQuestionKnowledge;
import com.extjs.service.EKnowledgeService;
import com.extjs.service.EQuestionKnowledgeService;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Scope("prototype")
@Transactional
public class EQuestionKnowledgeServiceImpl implements EQuestionKnowledgeService {
    @Autowired
    private EKnowledgeService eKnowledgeService;
    @Autowired
    private EQuestionKnowledgeDao eQuestionKnowledgeDao;

    @Override
    public List<EKnowledge> knowledgeCheckedOrNot(String questionID, List<EKnowledge> knowledges) {
        List<EKnowledge> resultList = new ArrayList<>();
        if (null != questionID && questionID.length() > 0) {
            EQuestionKnowledge questionKnowledge = new EQuestionKnowledge();
            questionKnowledge.setQuestionid(questionID);
            List<EQuestionKnowledge> questionKnowledges = eQuestionKnowledgeDao.getQuestionKnowledges(questionKnowledge);
            Map<String, String> questionKnowledgeMap = new HashMap<>();
            for (EQuestionKnowledge eQuestionKnowledge : questionKnowledges) {
                questionKnowledgeMap.put(eQuestionKnowledge.getKnowledgeid(), eQuestionKnowledge.getQuestionid());
            }

            EKnowledge cknowledge = null;
            for (EKnowledge knowledge : knowledges) {
                cknowledge = new EKnowledge();
                ReflectionUtil.copyProperties(knowledge, cknowledge);
                if (null != questionKnowledgeMap.get(knowledge.getId())) {
                    cknowledge.setOnChecked(true);
                } else {
                    cknowledge.setOnChecked(false);
                }
                resultList.add(cknowledge);
            }
        } else {
            resultList = knowledges;
        }

        return resultList;
    }

    @Override
    public List<EQuestionKnowledge> getQuestionKnowledges(EQuestionKnowledge eQuestionKnowledge) {
        List<EQuestionKnowledge> questionKnowledges = eQuestionKnowledgeDao.getQuestionKnowledges(eQuestionKnowledge);
        return questionKnowledges;
    }

    @Override
    public void addQuestionKnowledge(EQuestionKnowledge eQuestionKnowledge) throws SysException {
        eQuestionKnowledgeDao.addQuestionKnowledge(eQuestionKnowledge);
    }

    @Override
    public void delQuestionKnowledge(EQuestionKnowledge eQuestionKnowledge) throws SysException {
        eQuestionKnowledgeDao.delQuestionKnowledge(eQuestionKnowledge);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void addSelected(String questionID, String knowledgeIDS) throws SysException {
        if (null != questionID && questionID.length() > 0) {
            EQuestionKnowledge questionKnowledge = new EQuestionKnowledge();
            questionKnowledge.setQuestionid(questionID);
            if (null != knowledgeIDS && knowledgeIDS.length() > 0) {
                eQuestionKnowledgeDao.delQuestionKnowledge(questionKnowledge);//先删除指定题目ID的数据
                String[] konwledges = knowledgeIDS.split(",");//拆分已选id
                for (int i = 0; i < konwledges.length; i++) {
                    questionKnowledge = new EQuestionKnowledge();
                    questionKnowledge.setQuestionid(questionID);
                    questionKnowledge.setKnowledgeid(konwledges[i]);
                    EKnowledge eKnowledge = eKnowledgeService.getKnowledge(konwledges[i]);
                    questionKnowledge.setKnowledgetext(eKnowledge.getKnowledgeText());
                    questionKnowledge.setId(UUID.randomUUID().toString());
                    eQuestionKnowledgeDao.addQuestionKnowledge(questionKnowledge);
                }
            }
        }
    }
}
