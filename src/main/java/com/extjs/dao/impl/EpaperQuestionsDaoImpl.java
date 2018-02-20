package com.extjs.dao.impl;

import com.extjs.dao.EpaperQuestionsDao;
import com.extjs.model.EPaperQuestions;
import com.extjs.model.VPaperQuestionAndInfo;
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
        Session session = sessionFactory.getCurrentSession();
        Query query;
        if (null != ePaperQuestions.getQuestionid() && ePaperQuestions.getQuestionid().length() > 0) {
            query = session.createQuery("delete from EPaperQuestions where questionid='" + ePaperQuestions.getQuestionid() + "'");
            query.executeUpdate();
            session.flush();
        } else {
            if (null != ePaperQuestions.getId() && ePaperQuestions.getId().length() > 0) {
                query = session.createQuery("delete from epaperquestions where id='" + ePaperQuestions.getId() + "'");
                query.executeUpdate();
                session.flush();

            }
        }
    }

    @Override
    public void mergePaperQuestion(EPaperQuestions paperQuestions) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(paperQuestions);
        session.flush();
    }

    @Override
    public List<VPaperQuestionAndInfo> getPaperQuestionAndInfo(VPaperQuestionAndInfo vPaperQuestionAndInfo) {
        StringBuilder hql = new StringBuilder("from VPaperQuestionAndInfo WHERE 1=1");
        if (null != vPaperQuestionAndInfo.getId() && vPaperQuestionAndInfo.getId().length() > 0) {
            hql.append(" and id='" + vPaperQuestionAndInfo.getId() + "'");
        } else {
            if (null != vPaperQuestionAndInfo.getPaperid() && vPaperQuestionAndInfo.getPaperid().length() > 0) {

                hql.append(" and paperid='" + vPaperQuestionAndInfo.getPaperid() + "'");
            }
            if (null != vPaperQuestionAndInfo.getSchoolno() && vPaperQuestionAndInfo.getSchoolno().length() > 0 && !"noselected".equals(vPaperQuestionAndInfo.getSchoolno())) {
                hql.append(" and schoolno='" + vPaperQuestionAndInfo.getSchoolno() + "'");
            }
            if (null != vPaperQuestionAndInfo.getGradeno() && vPaperQuestionAndInfo.getGradeno().length() > 0 && !"noselected".equals(vPaperQuestionAndInfo.getGradeno())) {
                hql.append(" and gradeno='" + vPaperQuestionAndInfo.getGradeno() + "'");
            }
            if (null != vPaperQuestionAndInfo.getSubjectno() && !"".equals(vPaperQuestionAndInfo.getSubjectno())) {
                hql.append(" and subjectno='" + vPaperQuestionAndInfo.getSubjectno() + "'");
            }
            if (null != vPaperQuestionAndInfo.getQuestionid() && !"".equals(vPaperQuestionAndInfo.getQuestionid())) {
                hql.append(" and questionid='" + vPaperQuestionAndInfo.getQuestionid() + "'");
            }
//            if (null != vPaperQuestionAndInfo.getQuestiontype() && vPaperQuestionAndInfo.getQuestiontype().length() > 0 && !"noselected".equals(vPaperQuestionAndInfo.getQuestiontype())) {
//                hql.append(" and questiontype='" + vPaperQuestionAndInfo.getQuestiontype() + "'");
//            }
            if (null != vPaperQuestionAndInfo.getCreator() && !"".equals(vPaperQuestionAndInfo.getCreator())) {
                hql.append(" and creator='" + vPaperQuestionAndInfo.getCreator() + "'");
            }
//            if (null != vPaperQuestionAndInfo.getStartDate() && null != vPaperQuestionAndInfo.getEndDate()) {
//                hql.append(" and testdate BETWEEN to_date('" + vPaperQuestionAndInfo.getStartDate() + "', 'yyyy-mm-dd') AND to_date('" + vPaperQuestionAndInfo.getEndDate() + "', 'yyyy-mm-dd')");
//            }
//            if (null != vPaperQuestionAndInfo.getDifficulty() && !"".equals(vPaperQuestionAndInfo.getDifficulty())) {
//                hql.append(" and difficulty=" + vPaperQuestionAndInfo.getDifficulty());
//            }
//            if (null != vPaperQuestionAndInfo.getQuestion() && !"".equals(vPaperQuestionAndInfo.getQuestion())) {
//                hql.append(" and question like '%" + vPaperQuestionAndInfo.getQuestion() + "%'");
//            }
        }
        try {
            hql.append(" order by paperid,paperquestionno,questionno");
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(hql.toString());
            List<VPaperQuestionAndInfo> paperQuestionAndInfoList = query.list();
            return paperQuestionAndInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
