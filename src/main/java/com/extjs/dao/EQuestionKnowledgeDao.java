package com.extjs.dao;

import com.extjs.model.EQuestionKnowledge;
import com.extjs.util.SysException;

import java.util.List;

public interface EQuestionKnowledgeDao {
    List<EQuestionKnowledge> getQuestionKnowledges(EQuestionKnowledge questionKnowledge);
    void addQuestionKnowledge(EQuestionKnowledge questionKnowledge) throws SysException;
    void delQuestionKnowledge(EQuestionKnowledge questionKnowledge) throws SysException;
}
