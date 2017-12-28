package com.extjs.service;

import com.extjs.model.EKnowledge;
import com.extjs.model.EQuestionKnowledge;
import com.extjs.util.SysException;

import java.util.List;

public interface EQuestionKnowledgeService {
    /**
     * 判断指定集合中的数据id，是否已被选中（题目_知识点表中存在记录，则表示选中）
     *
     * @return
     */
    List<EKnowledge> knowledgeCheckedOrNot(String questionID, List<EKnowledge> knowledges);

    List<EQuestionKnowledge> getQuestionKnowledges(EQuestionKnowledge eQuestionKnowledge);

    void addQuestionKnowledge(EQuestionKnowledge eQuestionKnowledge) throws SysException;

    void delQuestionKnowledge(EQuestionKnowledge eQuestionKnowledge) throws SysException;

    void addSelected(String questionID, String knowledgeIDS) throws SysException;
}
