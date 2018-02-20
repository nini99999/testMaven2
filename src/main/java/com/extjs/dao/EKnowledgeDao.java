package com.extjs.dao;

import com.extjs.model.EKnowledge;
import com.extjs.model.EPaperKnowledgeDTO;
import com.extjs.model.EWrongKnowledgeDTO;
import com.extjs.util.SysException;

import java.util.List;

public interface EKnowledgeDao {
    List<EKnowledge> queryKnowledge(EKnowledge knowledge);

    List<EKnowledge> getKnowledgeContainsChilds(String id);

    List<EKnowledge> getKnowledgeChilds(String id);

    void addKnowledge(EKnowledge knowledge) throws SysException;

    void modifKnowledge(EKnowledge knowledge) throws SysException;

    void saveKnowledgeTextOnly(String id, String knowledgeText) throws SysException;

    void delKnowledge(String id) throws SysException;

    EKnowledge getKnowledgeByID(String id);

    List<EWrongKnowledgeDTO> getWrongKnowledge(EWrongKnowledgeDTO wrongKnowledgeDTO);//查询指定学生、知识点的错题信息

    List<EPaperKnowledgeDTO> getPaperKnowledge(String knowledgeID, String studentID);//查指定学生、指定知识点总共的做题信息
}
