package com.extjs.dao.impl;

import com.extjs.dao.EquestionDao;
import com.extjs.model.EQuestions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jenny on 2017/6/20.
 */
@Repository
@Scope("prototype")
public class EquestionsDaoImpl implements EquestionDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<EQuestions> getQuestions(EQuestions eQuestions) {
        StringBuilder sb = new StringBuilder();
        sb.append("from EQuestions where 1=1");
        if (null != eQuestions.getId() && !"".equals(eQuestions.getId())) {
            sb.append(" and id='" + eQuestions.getId() + "'");
        } else {
//            if (null != eQuestions.getQtype() && !"".equals(eQuestions.getQtype())) {
//                sb.append(" and qtype='" + eQuestions.getQtype() + "'");
//            }
            if (null != eQuestions.getQuestionid() && !"".equals(eQuestions.getQuestionid())) {
                sb.append(" and questionid='" + eQuestions.getQuestionid() + "'");
            }
            if (null != eQuestions.getQuestionno() && !"".equals(eQuestions.getQuestionno())) {
                sb.append("and questionno='" + eQuestions.getQuestionno() + "'");
            }

        }
        sb.append(" order by questionid desc,questionno");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        List<EQuestions> eQuestionsList = query.list();
        return eQuestionsList;
    }

    @Override
    public String addQuestion(EQuestions eQuestions) {
        String flag = "";
        try {
            Session session = sessionFactory.getCurrentSession();
            session.merge(eQuestions);
//            session.saveOrUpdate(eQuestions);
            session.flush();
        } catch (Exception e) {
            e.printStackTrace();
            flag = "error";
        }
        return flag;
    }

    @Override
    public void delQuestion(String questionID) {
        Session session = sessionFactory.getCurrentSession();
        EQuestions questions = new EQuestions();
        questions.setQuestionid(questionID);
        List<EQuestions> questionsList = this.getQuestions(questions);
        for (EQuestions eQuestions : questionsList) {
            session.delete(eQuestions);
        }
        session.flush();
    }

    @Override
    public EQuestions updateQuestion(EQuestions eQuestions) {
        Session session = sessionFactory.getCurrentSession();
        session.update(eQuestions);
        session.flush();
        return eQuestions;
    }
}
