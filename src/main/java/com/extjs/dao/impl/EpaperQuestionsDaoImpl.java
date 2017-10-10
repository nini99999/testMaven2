package com.extjs.dao.impl;

import com.extjs.dao.EpaperQuestionsDao;
import com.extjs.model.EPaperQuestions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jenny on 2017/6/22.
 */
@Repository
@Scope("prototype")
public class EpaperQuestionsDaoImpl implements EpaperQuestionsDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<EPaperQuestions> getPaperQuestions(EPaperQuestions ePaperQuestions) {
        StringBuilder sb = new StringBuilder();
        sb.append("from EPaperQuestions where 1=1");
        if (null != ePaperQuestions.getQuestionid() && !"".equals(ePaperQuestions.getQuestionid())) {
            sb.append(" and id='" + ePaperQuestions.getId() + "'");
        } else {
            if (null != ePaperQuestions.getQuestionid() && !"".equals(ePaperQuestions.getQuestionid())) {
                sb.append(" and questionid='" + ePaperQuestions.getQuestionid() + "'");
            }
            if (null != ePaperQuestions.getPaperid() && !"".equals(ePaperQuestions.getPaperid())) {
                sb.append(" and paperid='" + ePaperQuestions.getPaperid() + "'");
            }
            if (null != ePaperQuestions.getCreator() && !"".equals(ePaperQuestions.getCreator())) {
                sb.append(" and creator='" + ePaperQuestions.getCreator() + "'");
            }
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        List<EPaperQuestions> ePaperQuestionsList = query.list();
        return ePaperQuestionsList;
    }

    @Override
    public void addPaperQuestions(EPaperQuestions ePaperQuestions) {
        Session session = sessionFactory.getCurrentSession();
        session.save(ePaperQuestions);
        session.flush();
    }

    @Override
    public EPaperQuestions updatePaperQuestions(EPaperQuestions ePaperQuestions) {
        Session session = sessionFactory.getCurrentSession();
        session.update(ePaperQuestions);
        session.flush();
        return ePaperQuestions;
    }

    @Override
    public void delPaperQuestions(EPaperQuestions ePaperQuestions) {

    }
}
