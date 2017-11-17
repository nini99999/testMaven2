package com.extjs.dao.impl;

import com.extjs.dao.EStudentMarkDao;
import com.extjs.model.EStudentMark;
import com.extjs.model.Page;
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
    public List<EStudentMark> queryEStudentMark(EStudentMark eStudentMark, Page page) {
        List<EStudentMark> studentMarks = new ArrayList<EStudentMark>();
        String hql = this.getHql(eStudentMark);
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        if (null != page.getPageno() && page.getPageno() > 0 && null != page.getPagesize() && page.getPagesize() > 0) {
            query.setFirstResult((page.getPageno()-1)*page.getPagesize());
            query.setMaxResults(page.getPagesize());
        }
        studentMarks = query.list();
        return studentMarks;
    }

    @Override
    public int getTotalCount(EStudentMark eStudentMark) {
        StringBuilder sb = new StringBuilder("select count(rowid) ");
        sb.append(this.getHql(eStudentMark)).append(")");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        int res = Integer.parseInt(query.uniqueResult().toString());
        return res;
    }


    private String getHql(EStudentMark eStudentMark) {
        StringBuilder sb = new StringBuilder("from EStudentMark where 1=1");
        if (null != eStudentMark.getCreator() && !"".equals(eStudentMark.getCreator())) {
            sb.append(" and creator='").append(eStudentMark.getCreator()).append("'");

        }
        if (null != eStudentMark.getStudentname() && !"".equals(eStudentMark.getStudentname())) {
            sb.append(" and studentname='" + eStudentMark.getStudentname() + "'");
        }
        if (null != eStudentMark.getStudentno() && eStudentMark.getStudentno().length() > 0) {
            sb.append(" and studentno='" + eStudentMark.getStudentno() + "'");
        }
        if (null != eStudentMark.getSubjectno() && !"".equals(eStudentMark.getSubjectno()) && !"noselected".equals(eStudentMark.getSubjectno())) {
            sb.append(" and subjectno='" + eStudentMark.getSubjectno() + "'");
        }
        if (null != eStudentMark.getTpname() && !"".equals(eStudentMark.getTpname())) {
            sb.append(" and tpname like '%" + eStudentMark.getTpname() + "%'");
        }
        if (null != eStudentMark.getTpno() && eStudentMark.getTpno().length() > 0) {
            sb.append(" and tpno='" + eStudentMark.getTpno() + "'");
        }
        if (null != eStudentMark.getClassno() && eStudentMark.getClassno().length() > 0) {
            sb.append(" and classno='" + eStudentMark.getClassno() + "'");
        }
        sb.append(" order by tpno,mark desc,createdate desc");
        return sb.toString();
    }

    @Override
    public void saveOrUpdateEStudentMark(EStudentMark eStudentMark) {
        Session session = sessionFactory.getCurrentSession();
        if (null != eStudentMark) {
//            session.saveOrUpdate(eStudentMark);
            session.merge(eStudentMark);
            session.flush();
        }
    }

    @Override
    public void modifOnlyMark(EStudentMark studentMark) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("update EStudentMark set markone=:markone,marktwo=:marktwo,mark=:mark where id=:id");
        query.setFloat("markone", studentMark.getMarkone());
        query.setFloat("marktwo", studentMark.getMarktwo());
        query.setFloat("mark", studentMark.getMarkone() + studentMark.getMarktwo());
        query.setString("id", studentMark.getId());
        query.executeUpdate();
        session.flush();
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
