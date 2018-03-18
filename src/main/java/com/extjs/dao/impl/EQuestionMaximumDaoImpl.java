package com.extjs.dao.impl;

import com.extjs.dao.EQuestionMaximumDao;
import com.extjs.model.EQuestionMaximum;
import com.extjs.util.SysException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Scope("prototype")
public class EQuestionMaximumDaoImpl implements EQuestionMaximumDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public EQuestionMaximum getQuestionMaximum(String id) {
        if (null != id && id.length() > 0) {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery("from EQuestionMaximum where id='" + id + "'");
            EQuestionMaximum questionMaximum = (EQuestionMaximum) query.uniqueResult();
            return questionMaximum;
        } else {
            return null;
        }
    }

    @Override
    public List<EQuestionMaximum> getQuestionMaximum(EQuestionMaximum questionMaximum) {
        StringBuilder stringBuilder = new StringBuilder("from  EQuestionMaximum where 1=1");
        if (null != questionMaximum.getId() && questionMaximum.getId().length() > 0) {
            stringBuilder.append(" and id='" + questionMaximum.getId() + "'");
        } else {
            if (null != questionMaximum.getPaperId() && questionMaximum.getPaperId().length() > 0) {
                stringBuilder.append(" and paperId='" + questionMaximum.getPaperId() + "'");
            }
            if (null != questionMaximum.getQuestionIndex()) {
                stringBuilder.append(" and questionIndex=" + questionMaximum.getQuestionIndex());
            }
            if (null != questionMaximum.getQuestionTypeId() && questionMaximum.getQuestionTypeId().length() > 0) {
                stringBuilder.append(" and questionTypeId='" + questionMaximum.getQuestionTypeId() + "'");
            }
        }
        stringBuilder.append(" order by paperId,questionTypeId,questionIndex");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(stringBuilder.toString());
        List<EQuestionMaximum> eQuestionMaximums = query.list();
        return eQuestionMaximums;


    }

    @Override
    public Float getSumMaximum(String paperId, String questionTypeId) {
        StringBuilder stringBuilder = new StringBuilder("select sum(maximum) from EQuestionMaximum where 1=1");
        if (null != paperId && paperId.length() > 0 && null != questionTypeId && questionTypeId.length() > 0) {
            stringBuilder.append(" and paperId='" + paperId + "'").append(" and questionTypeId='" + questionTypeId + "'");
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(stringBuilder.toString());
            Float res = Float.parseFloat(query.uniqueResult().toString());
            return res;
        } else {
            return null;
        }
    }

    @Override
    public void addQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException {
        Session session = sessionFactory.getCurrentSession();
        session.save(questionMaximum);
    }

    @Override
    public void updateQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException {
        Session session = sessionFactory.getCurrentSession();
        session.update(questionMaximum);
    }

    @Override
    public void modifQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException {
        Session session = sessionFactory.getCurrentSession();
        session.merge(questionMaximum);
        session.flush();
    }

    @Override
    public void delQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException {
        StringBuilder stringBuilder = new StringBuilder();
        if (null != questionMaximum.getId() && questionMaximum.getId().length() > 0) {
            stringBuilder.append("delete from  EQuestionMaximum where id='" + questionMaximum.getId() + "'");
        } else {
            if (null != questionMaximum.getPaperId() && questionMaximum.getPaperId().length() > 0) {
                stringBuilder.append("delete from  EQuestionMaximum where paperId='" + questionMaximum.getPaperId() + "'");
            }
            if (null != questionMaximum.getQuestionTypeId() && questionMaximum.getQuestionTypeId().length() > 0) {
                stringBuilder.append(" and questionTypeId='" + questionMaximum.getQuestionTypeId() + "'");
            }
            if (null != questionMaximum.getQuestionIndex() && questionMaximum.getQuestionIndex() > 0) {
                stringBuilder.append(" and questionIndex=" + questionMaximum.getQuestionIndex());
            }
        }
        if (stringBuilder.toString().length() > 0) {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(stringBuilder.toString());
            query.executeUpdate();
        }

    }
}
