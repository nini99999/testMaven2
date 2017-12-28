package com.extjs.dao;

import com.extjs.model.EKnowledge;
import com.extjs.util.SysException;

import java.util.List;

public interface EKnowledgeDao {
    List<EKnowledge> queryKnowledge(EKnowledge knowledge);

    List<EKnowledge> getKnowledgeContainsChilds(String id);

    List<EKnowledge> getKnowledgeChilds(String id);

    void addKnowledge(EKnowledge knowledge) throws SysException;

    void modifKnowledge(EKnowledge knowledge) throws SysException;

    void saveKnowledgeTextOnly(String id,String knowledgeText) throws SysException;

    void delKnowledge(String id) throws SysException;

    EKnowledge getKnowledgeByID(String id);
}
