package com.extjs.dao.impl;

import com.extjs.dao.EquestionInfoDao;
import com.extjs.model.EQuestionInfo;
import com.extjs.model.VQuestionandinfo;
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
public class EquestionInfoDaoImpl implements EquestionInfoDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<EQuestionInfo> getQuestionInfo(EQuestionInfo eQuestionInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("from EQuestionInfo where 1=1");
        if (null != eQuestionInfo.getId() && !"".equals(eQuestionInfo.getId())) {
            sb.append(" and id='" + eQuestionInfo.getId() + "'");
        } else {
            if (null != eQuestionInfo.getQuestionid() && !"".equals(eQuestionInfo.getQuestionid())) {
                sb.append(" and questionid='" + eQuestionInfo.getQuestionid() + "'");
            }
            if (null != eQuestionInfo.getQuestiontype() && !"".equals(eQuestionInfo.getQuestiontype())) {
                sb.append(" and questiontype='" + eQuestionInfo.getQuestiontype() + "'");
            }
            if (null != eQuestionInfo.getCreator() && !"".equals(eQuestionInfo.getCreator())) {
                sb.append(" and creator='" + eQuestionInfo.getCreator() + "'");
            }
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        List<EQuestionInfo> questionInfoList = query.list();
        return questionInfoList;
    }

    @Override
    public void saveOrUpdateQuestionInfo(EQuestionInfo eQuestionInfo) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(eQuestionInfo);
        session.flush();
    }

    @Override
    public void delQuestionInfo(String questionid) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete from EQuestionInfo where questionid='" + questionid + "'");
        query.executeUpdate();
        session.flush();
    }

    @Override
    public List<VQuestionandinfo> getQuestionAndInfos(VQuestionandinfo questionandinfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("from VQuestionandinfo where 1=1");
        if (null != questionandinfo.getId() && !"".equals(questionandinfo.getId())) {
            sb.append(" and id='" + questionandinfo.getId() + "'");
        } else {
            if (null != questionandinfo.getSchoolno() && questionandinfo.getSchoolno().length() > 0 && !"noselected".equals(questionandinfo.getSchoolno())) {
                sb.append(" and schoolno='" + questionandinfo.getSchoolno() + "'");
            }
            if (null != questionandinfo.getGradeno() && questionandinfo.getGradeno().length() > 0 && !"noselected".equals(questionandinfo.getGradeno())) {
                sb.append(" and gradeno='" + questionandinfo.getGradeno() + "'");
            }
            if (null != questionandinfo.getSubjectno() && !"".equals(questionandinfo.getSubjectno())) {
                sb.append(" and subjectno='" + questionandinfo.getSubjectno() + "'");
            }
            if (null != questionandinfo.getQuestionid() && !"".equals(questionandinfo.getQuestionid())) {
                sb.append(" and questionid='" + questionandinfo.getQuestionid() + "'");
            }
            if (null != questionandinfo.getQuestiontype() && questionandinfo.getQuestiontype().length() > 0 && !"noselected".equals(questionandinfo.getQuestiontype())) {
                sb.append(" and questiontype='" + questionandinfo.getQuestiontype() + "'");
            }
            if (null != questionandinfo.getCreator() && !"".equals(questionandinfo.getCreator())) {
                sb.append(" and creator='" + questionandinfo.getCreator() + "'");
            }
            if (null != questionandinfo.getDifficulty() && !"".equals(questionandinfo.getDifficulty())) {
                sb.append(" and difficulty=" + questionandinfo.getDifficulty());
            }
            if (null != questionandinfo.getQuestion() && !"".equals(questionandinfo.getQuestion())) {
                sb.append(" and question like '%" + questionandinfo.getQuestion() + "%'");
            }
        }
        sb.append(" order by questionid,questionno");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        List<VQuestionandinfo> res = query.list();
        return res;
    }
}
