package com.extjs.dao.impl;

import com.extjs.dao.EAnswerDao;
import com.extjs.model.EAnswer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * Created by jenny on 2017/11/21.
 */
@Repository
@Scope("prototype")
public class EAnswerDaoImpl implements EAnswerDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public EAnswer getAnswer(String id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from EAnswer where id='" + id + "'");
        EAnswer answer = (EAnswer) query.uniqueResult();
        return answer;
    }

    @Override
    public void saveAnswer(EAnswer answer) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(answer);
        session.flush();
    }

    @Override
    public void delAnswer(String id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete from EAnswer where id='" + id + "'");
        query.executeUpdate();
    }
}
