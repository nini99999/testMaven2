package com.extjs.dao;

import com.extjs.model.EWrongKnowledgeDTO;

import java.util.List;

public interface EWrongknowledgeDao {

    List<EWrongKnowledgeDTO> getWrongKnowledges(EWrongKnowledgeDTO wrongKnowledgeDTO);
}
