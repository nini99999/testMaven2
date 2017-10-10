package com.extjs.dao.impl;

import com.extjs.dao.ReportDao;
import com.extjs.model.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jenny on 2017/4/9.
 */
@Repository
@Scope("prototype")
public class ReportDaoImpl implements ReportDao {
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 查询班级成绩表
     *
     * @param rClassMark
     * @return
     */
    @Override
    public List<RClassMark> queryRClassMark(RClassMark rClassMark) {
        List<RClassMark> rClassMarkList = new ArrayList<RClassMark>();
        StringBuilder sb = new StringBuilder("from RClassMark where 1=1");
        if (null != rClassMark.getTpno() && !"".equals(rClassMark.getTpno())) {
            sb.append(" and tpno in (" + rClassMark.getTpno() + ")");
        }
        if (null != rClassMark.getGradeno() && !"".equals(rClassMark.getGradeno())) {
            sb.append(" and gradeno='" + rClassMark.getGradeno() + "'");
        }
        if (null != rClassMark.getClassno() && !"".equals(rClassMark.getClassno())) {
            sb.append(" and classno='" + rClassMark.getClassno() + "'");
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        rClassMarkList = query.list();
        return rClassMarkList;
    }

    @Override
    public void addRClassMark(RClassMark rClassMark) {

    }

    /**
     * 删除班级成绩表
     *
     * @param creator，当前用户
     */
    @Override
    public void delRClassMark(String creator) {
        String hql = "delete RClassMark where creator=:str";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(hql).setString("str", creator).executeUpdate();
        session.flush();
    }

    @Override
    public List<RMarkArea> queryRMarkArea(RMarkArea rMarkArea) {
        List<RMarkArea> rMarkAreaList = new ArrayList<RMarkArea>();
        StringBuilder sb = new StringBuilder("from RMarkArea where 1=1");
        if (null != rMarkArea.getClassno() && !"".equals(rMarkArea.getClassno())) {
            sb.append(" and classno='" + rMarkArea.getClassno() + "'");
        }
        if (null != rMarkArea.getGradeno() && !"".equals(rMarkArea.getGradeno())) {
            sb.append(" and gradeno='" + rMarkArea.getGradeno() + "'");
        }
        if (null != rMarkArea.getCreator() && !"".equals(rMarkArea.getCreator())) {
            sb.append(" and creator='" + rMarkArea.getCreator() + "'");
        }
        if (null != rMarkArea.getSubjectno() && !"".equals(rMarkArea.getSubjectno())) {
            sb.append(" and subjectno='" + rMarkArea.getSubjectno() + "'");
        } else {
            sb.append(" and subjectno='noselected'");
        }
        sb.append(" order by classno,to_number(markarea)");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        rMarkAreaList = query.list();

        return rMarkAreaList;
    }

    @Override
    public void addRMarkArea(RMarkArea rMarkArea) {

    }

    @Override
    public void delRMarkArea(String creator) {
        String hql = "delete RMarkArea where creator=:str";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(hql).setString("str", creator).executeUpdate();
        session.flush();

    }

    @Override
    public List<RWrongQuestion> queryRWrongQuestion(RWrongQuestion rWrongQuestion) {
        List<RWrongQuestion> wrongQuestionList = new ArrayList<RWrongQuestion>();
        StringBuilder sb = new StringBuilder("from RWrongQuestion where 1=1");
        if (null != rWrongQuestion.getCreator() && !"".equals(rWrongQuestion.getCreator())) {
            sb.append(" and creator='" + rWrongQuestion.getCreator() + "'");
        }
        sb.append(" order by wrongnums desc");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        wrongQuestionList = query.list();
        return wrongQuestionList;
    }

    @Override
    public void addRWrongQuestion(RWrongQuestion rWrongQuestion) {

    }

    @Override
    public void delRWrongQuestion(String creator) {
        String hql = "delete RWrongQuestion where creator=:str";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(hql).setString("str", creator).executeUpdate();
        session.flush();
    }

    @Override
    public List<RAboveSpecifiedMark> queryRAboveSpecifiedMark(RAboveSpecifiedMark aboveSpecifiedMark) {
        List<RAboveSpecifiedMark> aboveSpecifiedMarks = new ArrayList<RAboveSpecifiedMark>();
        StringBuilder sb = new StringBuilder("from RAboveSpecifiedMark where 1=1");
        if (null != aboveSpecifiedMark.getCreator() && !"".equals(aboveSpecifiedMark.getCreator())) {
            sb.append(" and creator='" + aboveSpecifiedMark.getCreator() + "'");
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        aboveSpecifiedMarks = query.list();
        return aboveSpecifiedMarks;
    }

    @Override
    public void addRAboveSpecifiedMark(RAboveSpecifiedMark aboveSpecifiedMark) {

    }

    @Override
    public void delRAboveSpecifiedMark(String creator) {
        String hql = "delete RAboveSpecifiedMark where creator=:str";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(hql).setString("str", creator).executeUpdate();
        session.flush();
    }

    @Override
    public HashMap<String, Integer> getRAboveSpecifiedMark(RAboveSpecifiedMark aboveSpecifiedMark) {
        HashMap resultMap = new HashMap();
        List<RAboveSpecifiedMark> aboveSpecifiedMarks = this.queryRAboveSpecifiedMark(aboveSpecifiedMark);
        for (RAboveSpecifiedMark rAboveSpecifiedMark : aboveSpecifiedMarks) {
            resultMap.put(rAboveSpecifiedMark.getClassno(), rAboveSpecifiedMark.getAbovemark());
        }

        return resultMap;
    }

    @Override
    public List<RYearMark> queryRYearMark(RYearMark rYearMark) {
        List<RYearMark> rYearMarks = new ArrayList<RYearMark>();
        StringBuilder sb = new StringBuilder("from RYearMark where 1=1");
        if (null != rYearMark.getCreator() && !"".equals(rYearMark.getCreator())) {
            sb.append(" and creator='" + rYearMark.getCreator() + "'");
        }
        if (null != rYearMark.getYear() && !"".equals(rYearMark.getYear())) {
            sb.append(" and year=" + rYearMark.getYear());
        }
        if (null != rYearMark.getSubjectno() && !"".equals(rYearMark.getSubjectno()) && !"noselected".equals(rYearMark.getSubjectno())) {
            sb.append(" and subjectno='" + rYearMark.getSubjectno() + "'");
        }
        if (null != rYearMark.getClassno() && !"".equals(rYearMark.getClassno())) {
            sb.append(" and classno='" + rYearMark.getClassno() + "'");
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        rYearMarks = query.list();
        return rYearMarks;
    }

    public void addRYearMark(RYearMark rYearMark) {

    }

    public void delRYearMark(String creator) {
        String hql = "delete RYearMark where creator=:str";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(hql).setString("str", creator).executeUpdate();
        session.flush();
    }

    public List<RYearMarkStudent> queryRYearMarkStudent(RYearMarkStudent rYearMarkStudent) {
        List<RYearMarkStudent> rYearMarkStudents = new ArrayList<RYearMarkStudent>();
        StringBuilder sb = new StringBuilder("from RYearMarkStudent where 1=1");
        if (null != rYearMarkStudent.getCreator() && !"".equals(rYearMarkStudent.getCreator())) {
            sb.append(" and creator='" + rYearMarkStudent.getCreator() + "'");
        }
        if (null != rYearMarkStudent.getSubjectno() && !"".equals(rYearMarkStudent.getSubjectno())) {
            sb.append(" and subjectno='" + rYearMarkStudent.getSubjectno() + "'");
        }
        if (null != rYearMarkStudent.getStudentname() && !"".equals(rYearMarkStudent.getStudentname())) {
            sb.append(" and studentname like '%" + rYearMarkStudent.getStudentname() + "%'");
        }
        if (null != rYearMarkStudent.getYear() && !"".equals(rYearMarkStudent.getYear())) {
            sb.append(" and year=" + rYearMarkStudent.getYear());
        }
        if (null != rYearMarkStudent.getStudentno() && !"".equals(rYearMarkStudent.getStudentno())) {
            sb.append(" and studentno='" + rYearMarkStudent.getStudentno() + "'");
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        rYearMarkStudents = query.list();
        return rYearMarkStudents;
    }

    public void addRYearMarkStudent(RYearMarkStudent rYearMarkStudent) {

    }

    public void delRYearMarkStudent(String creator) {
        String hql = "delete RYearMarkStudent where creator=:str";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(hql).setString("str", creator).executeUpdate();
        session.flush();
    }
}
