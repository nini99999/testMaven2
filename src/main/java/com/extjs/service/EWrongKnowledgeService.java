package com.extjs.service;

import com.extjs.model.EKnowledge;
import com.extjs.model.EWrongKnowledgeDTO;
import com.extjs.model.EWrongStudentDTO;
import com.extjs.util.SysException;

import java.util.List;

public interface EWrongKnowledgeService {

    /**
     * 查询错题、知识点对应视图
     *
     * @param EWrongKnowledgeDTO
     * @return
     * @throws SysException
     */
    List<EWrongKnowledgeDTO> getWrongKnowledges(EWrongKnowledgeDTO wrongKnowledgeDTO) throws SysException;
}
