package com.extjs.service.impl;

import com.extjs.dao.EWrongknowledgeDao;
import com.extjs.model.EWrongKnowledgeDTO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Scope("prototype")
@Repository
public class EWrongknowledgeDaoImpl implements EWrongknowledgeDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<EWrongKnowledgeDTO> getWrongKnowledges(EWrongKnowledgeDTO wrongKnowledgeDTO) {
        List<EWrongKnowledgeDTO> wrongKnowledgeDTOS = new ArrayList<>();
        StringBuilder hql = new StringBuilder("select * from v_wrong_knowledge where 1=1");
        if (null != wrongKnowledgeDTO.getKnowledgeID() && wrongKnowledgeDTO.getKnowledgeID().length() > 0) {
            hql.append(" and knowledgeid = '" + wrongKnowledgeDTO.getKnowledgeID() + "' ");
        }
        if (null != wrongKnowledgeDTO.getStudentID() && wrongKnowledgeDTO.getStudentID().length() > 0) {
            hql.append(" and studentid='" + wrongKnowledgeDTO.getStudentID() + "'");
        }
        if (null != wrongKnowledgeDTO.getQuestionID() && wrongKnowledgeDTO.getQuestionID().length() > 0) {
            hql.append(" and questionid='" + wrongKnowledgeDTO.getQuestionID() + "'");
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(hql.toString()).addScalar("QUESTIONID", StringType.INSTANCE)
                .addScalar("STUDENTID", StringType.INSTANCE)
                .addScalar("KNOWLEDGEID", StringType.INSTANCE);
        List list = query.list();
        EWrongKnowledgeDTO eWrongKnowledgeDTO = new EWrongKnowledgeDTO();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            eWrongKnowledgeDTO = new EWrongKnowledgeDTO();
            Object[] objects = (Object[]) iterator.next();
            eWrongKnowledgeDTO.setQuestionID(objects[0].toString());
            eWrongKnowledgeDTO.setStudentID(objects[1].toString());
            eWrongKnowledgeDTO.setKnowledgeID(objects[2].toString());
            wrongKnowledgeDTOS.add(eWrongKnowledgeDTO);
        }
        return wrongKnowledgeDTOS;
    }
}
