package com.extjs.dao.impl;


import com.extjs.dao.EwrongStudentDao;
import com.extjs.model.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.hibernate.Query;
import org.hibernate.Session;


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
