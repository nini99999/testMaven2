package com.extjs.dao.impl;

import com.extjs.dao.EQuestionKnowledgeDao;
import com.extjs.model.EQuestionKnowledge;
import com.extjs.util.SysException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Scope("prototype")
@Repository
public class EQuestionKnowledgeDaoImpl implements EQuestionKnowledgeDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<EQuestionKnowledge> getQuestionKnowledges(EQuestionKnowledge questionKnowledge) {
        StringBuilder stringBuilder = new StringBuilder("from EQuestionKnowledge where 1=1");
        if (null != questionKnowledge.getId() && questionKnowledge.getId().length() > 0) {
            stringBuilder.append(" and id='" + questionKnowledge.getId() + "'");
        } else {
            if (null != questionKnowledge.getQuestionid() && questionKnowledge.getQuestionid().length() > 0) {
                stringBuilder.append(" and questionid='" + questionKnowledge.getQuestionid() + "" + "'");
            }
            if (null != questionKnowledge.getKnowledgeid() && questionKnowledge.getKnowledgeid().length() > 0) {
                stringBuilder.append(" and knowledgeid='" + questionKnowledge.getKnowledgeid() + "'");
            }
            if (null != questionKnowledge.getKnowledgetext() && questionKnowledge.getKnowledgetext().length() > 0) {
                stringBuilder.append(" and knowledgetext like '%" + questionKnowledge.getKnowledgetext() + "%'");
            }
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(stringBuilder.toString());
        List<EQuestionKnowledge> questionKnowledges = query.list();
        return questionKnowledges;
    }

    @Override
    public void addQuestionKnowledge(EQuestionKnowledge questionKnowledge) throws SysException {
        if (null == questionKnowledge.getId() || questionKnowledge.getId().length() == 0) {
            questionKnowledge.setId(UUID.randomUUID().toString());
        }
        Session session = sessionFactory.getCurrentSession();
        session.save(questionKnowledge);
//        session.flush();
    }

    @Override
    public void delQuestionKnowledge(EQuestionKnowledge questionKnowledge) throws SysException {
        StringBuilder stringBuilder = new StringBuilder("delete from EQuestionKnowledge where 1=1");
        if (null != questionKnowledge.getId() && questionKnowledge.getId().length() > 0) {
            stringBuilder.append(" and id='" + questionKnowledge.getId() + "'");
        } else {
            if (null != questionKnowledge.getQuestionid() && questionKnowledge.getQuestionid().length() > 0) {
                stringBuilder.append(" and questionid='" + questionKnowledge.getQuestionid() + "" + "'");
            }
            if (null != questionKnowledge.getKnowledgeid() && questionKnowledge.getKnowledgeid().length() > 0) {
                stringBuilder.append(" and knowledgeid='" + questionKnowledge.getKnowledgeid() + "'");
            }
        }
        if (!"delete from EQuestionKnowledge where 1=1".equals(stringBuilder.toString())) {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(stringBuilder.toString());
            query.executeUpdate();
//        session.flush();
        }
    }
}
