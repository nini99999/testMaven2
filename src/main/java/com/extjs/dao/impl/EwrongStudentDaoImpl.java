package com.extjs.dao.impl;


import com.extjs.dao.EwrongStudentDao;
import com.extjs.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.hibernate.Query;
import org.hibernate.Session;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jenny on 2017/5/22.
 */
@Repository
@Scope("prototype")
public class EwrongStudentDaoImpl implements EwrongStudentDao {
    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public List<EWrongStudent> queryEWrongStudent(EWrongStudentDTO wrongStudent) {
        StringBuilder sb = new StringBuilder("from EWrongStudent where 1=1");
        if (null != wrongStudent.getStudentid() && wrongStudent.getStudentid().length() > 0) {
            sb.append(" and studentid='" + wrongStudent.getStudentid() + "'");
        }
        if (null != wrongStudent.getTestpaperno() && !"".equals(wrongStudent.getTestpaperno())) {
            sb.append(" and testpaperno='" + wrongStudent.getTestpaperno() + "'");
        }
        if (null != wrongStudent.getCountryid() && !"".equals(wrongStudent.getCountryid())) {
            sb.append(" and countryid='" + wrongStudent.getCountryid() + "'");
        }
        if (null != wrongStudent.getClassno() && wrongStudent.getClassno().length() > 0) {
            sb.append(" and studentid in (select id from EStudent where classno='" + wrongStudent.getClassno() + "')");
        }
        sb.append(" order by countryid,testpaperno,questionno");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        List<EWrongStudent> wrongStudents = query.list();
        return wrongStudents;
    }

    @Override
    public List<RWrongQuestion> getWrongsByDateArea(String beginDate, String endDate, String subjectno, String gradeno) {
        StringBuilder stringBuilder = new StringBuilder(
                "select count(*) as rownums,testpaperno,questionno,questionid from E_WRONG_STUDENT where testdate between to_date('")
                .append(beginDate).append("','yyyy-MM-dd') and to_date('")
                .append(endDate).append("','yyyy-MM-dd') and testpaperno in(select tpno from E_TESTPAPER where subjectno='")
                .append(subjectno).append("' and gradeno='" + gradeno + "') group by testpaperno,questionno,questionid order by rownums desc");

        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(stringBuilder.toString())
                .addScalar("rownums", StringType.INSTANCE)
                .addScalar("testpaperno", StringType.INSTANCE)
                .addScalar("questionno", StringType.INSTANCE).addScalar("questionid", StringType.INSTANCE);
        List list = query.list();
        List<RWrongQuestion> wrongQuestions = new ArrayList<>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Object[] objects = (Object[]) iterator.next();
            RWrongQuestion wrongQuestion = new RWrongQuestion();
            wrongQuestion.setWrongnums(Integer.parseInt(objects[0].toString()));
            wrongQuestion.setTpno(objects[1].toString());
            wrongQuestion.setQuestionno(objects[2].toString());
            wrongQuestion.setId(objects[3].toString());
            wrongQuestions.add(wrongQuestion);
        }

        return wrongQuestions;
    }

    @Override
    public void addEWrongStudent(EWrongStudent wrongStudent) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(wrongStudent);
        session.flush();
    }

    @Override
    public void delEWrongStudent(EWrongStudent wrongStudent) {
        StringBuilder sb = new StringBuilder("delete EWrongStudent where 1=1");
        if (null != wrongStudent.getId() && !"".equals(wrongStudent.getId())) {
            sb.append(" and id='" + wrongStudent.getId() + "'");
        } else {
            if (null != wrongStudent.getCountryid() && !"".equals(wrongStudent.getCountryid())) {
                sb.append(" and countryid='" + wrongStudent.getCountryid() + "'");
            }
            if (null != wrongStudent.getTestpaperno() && !"".equals(wrongStudent.getTestpaperno())) {
                sb.append(" and testpaperno='" + wrongStudent.getTestpaperno() + "'");
            }
            if (null != wrongStudent.getStudentid() && wrongStudent.getStudentid().length() > 0) {
                sb.append(" and studentid='" + wrongStudent.getStudentid() + "'");
            }
        }
        if (!"delete EWrongStudent where 1=1".equals(sb.toString())) {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(sb.toString());
            query.executeUpdate();
            session.flush();
        }
    }
}
