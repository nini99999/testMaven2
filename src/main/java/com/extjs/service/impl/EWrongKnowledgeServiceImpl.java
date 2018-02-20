package com.extjs.service.impl;

import com.extjs.dao.EWrongknowledgeDao;
import com.extjs.model.EKnowledge;
import com.extjs.model.EWrongKnowledgeDTO;
import com.extjs.model.EWrongStudentDTO;
import com.extjs.service.EWrongKnowledgeService;
import com.extjs.service.EquestionService;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope("prototype")
public class EWrongKnowledgeServiceImpl implements EWrongKnowledgeService {
    @Autowired
    EWrongknowledgeDao eWrongknowledgeDao;
@Autowired
    EquestionService equestionService;


    @Override
    public List<EWrongKnowledgeDTO> getWrongKnowledges(EWrongKnowledgeDTO wrongKnowledgeDTO) throws SysException {
        List<EWrongKnowledgeDTO> wrongKnowledgeDTOS = eWrongknowledgeDao.getWrongKnowledges(wrongKnowledgeDTO);
        List<EWrongKnowledgeDTO> result=new ArrayList<>();
        String question;
        for (EWrongKnowledgeDTO eWrongKnowledgeDTO:wrongKnowledgeDTOS){
            question="";
            if (null!=eWrongKnowledgeDTO.getQuestionID()&&eWrongKnowledgeDTO.getQuestionID().length()>0){
               question= equestionService.getOneQuestion(eWrongKnowledgeDTO.getQuestionID());
               eWrongKnowledgeDTO.setQuestion(question);
            }
            result.add(eWrongKnowledgeDTO);
        }
        return result;
    }
}
