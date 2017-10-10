package com.extjs.dao.impl;

import com.extjs.dao.EStudentMarkDao;
import com.extjs.model.EStudentMark;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jenny on 2017/5/12.
 */
@Repository
@Scope("prototype")

public class EStudentMarkDaoImpl implements EStudentMarkDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<EStudentMark> queryEStudentMark(EStudentMark eStudentMark) {
        List<EStudentMark> studentMarks = new ArrayList<EStudentMark>();
        StringBuilder sb = new StringBuilder("from EStudentMark where 1=1");
        if (null != eStudentMark.getCreator() && !"".equals(eStudentMark.getCreator())) {
            sb.append(" and creator='").append(eStudentMark.getCreator()).append("'");

        }
        if (null != eStudentMark.getStudentname() && !"".equals(eStudentMark.getStudentname())) {
            sb.append(" and studentname='" + eStudentMark.getStudentname() + "'");
        }
        if (null != eStudentMark.getSubjectno() && !"".equals(eStudentMark.getSubjectno()) && !"noselected".equals(eStudentMark.getSubjectno())) {
            sb.append(" and subjectno='" + eStudentMark.getSubjectno() + "'");
        }
        if (null != eStudentMark.getTpname() && !"".equals(eStudentMark.getTpname())) {
            sb.append(" and tpname like '%" + eStudentMark.getTpname() + "%'");
        }
        sb.append(" order by tpname,mark desc,createdate desc");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        studentMarks = query.list();
        return studentMarks;
    }

    @Override
    public void saveOrUpdateEStudentMark(EStudentMark eStudentMark) {
        Session session = sessionFactory.getCurrentSession();
        if (null != eStudentMark) {
            session.saveOrUpdate(eStudentMark);
            session.flush();
        }
    }

    @Override
    public void delEStudentMark(EStudentMark eStudentMark) {
        Session session = sessionFactory.getCurrentSession();
        if (null != eStudentMark) {
            session.delete(eStudentMark);
            session.flush();
        }
    }

    @Override
    public void delEStudentMarkByCreator(String creator) {
        Session session = sessionFactory.getCurrentSession();

        if (null != creator && !"".equals(creator)) {
            String sql = "delete EStudentMark where creator=:creator";
            session.createQuery(sql).setString("creator", creator).executeUpdate();
            session.flush();
        }

    }
}
