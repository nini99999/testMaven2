package com.extjs.dao.impl;

import com.extjs.dao.EstudentDao;
import com.extjs.model.EStudentDTO;
import com.extjs.model.EStudent;
import com.extjs.util.ReflectionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jenny on 2017/3/24.
 */
@Repository
@Scope("prototype")
public class EstudentDaoImpl implements EstudentDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<EStudent> queryEstudent(EStudentDTO eStudentDTO) {

        StringBuilder sb = new StringBuilder();
        sb.append("from EStudent where 1=1");
        if (null != eStudentDTO.getId() && eStudentDTO.getId().length() > 0) {
            sb.append(" and id='" + eStudentDTO.getId() + "'");
        }
        if (null != eStudentDTO.getSchoolno() && !"".equals(eStudentDTO.getSchoolno())) {
            sb.append(" and schoolno='" + eStudentDTO.getSchoolno() + "'");
        }
        if (null != eStudentDTO.getGradeno() && !"".equals(eStudentDTO.getGradeno())) {
            sb.append(" and gradeno='" + eStudentDTO.getGradeno() + "'");
        }
        if (null != eStudentDTO.getClassno() && !"".equals(eStudentDTO.getClassno())) {
            sb.append(" and classno='" + eStudentDTO.getClassno() + "'");
        }
        if (null != eStudentDTO.getUsername() && eStudentDTO.getUsername().length() > 0) {
            sb.append(" and username='" + eStudentDTO.getUsername() + "'");
        }

        Session session = sessionFactory.getCurrentSession();
        Query query = null;
        try {
            query = session.createQuery(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EStudent> eStudentList = query.list();

        return eStudentList;
    }

    @Override
    public List<EStudent> queryEstudentByClassAndTpno(String classno, String tpno) {
        StringBuilder stringBuilder = new StringBuilder("from EStudent where classno='");
        stringBuilder.append(classno + "' and id not in (SELECT studentno FROM EStudentMark where classno='")
                .append(classno + "' and tpno='").append(tpno + "')");
        Session session = sessionFactory.getCurrentSession();
        List<EStudent> studentList = new ArrayList<>();
        try {
            Query query = session.createQuery(stringBuilder.toString());
            studentList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentList;
    }

    @Override
    public EStudent getStudentByID(String id) {
        EStudent eStudent = new EStudent();
        if (null != id && id.length() > 0) {
            String hql = "from EStudent where id=:str";
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(hql).setString("str", id);
            eStudent = (EStudent) query.uniqueResult();
        }
        return eStudent;
    }

    public EStudent getEstudentByCountryID(String countryID) {
        EStudent eStudent = new EStudent();
        if (null != countryID && !"".equals(countryID)) {
            String hql = "from EStudent where countryid=:str";
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(hql).setString("str", countryID);
            eStudent = (EStudent) query.uniqueResult();
        }
        return eStudent;
    }

    @Override
    public String addEstudent(EStudentDTO eStudentDTO) {
        Session session = sessionFactory.getCurrentSession();
        EStudent eStudent = new EStudent();
        ReflectionUtil.copyProperties(eStudentDTO, eStudent);
        String flag = session.save(eStudent) + "";
        session.flush();
        return flag;
    }

    @Override
    public void delEstudent(EStudentDTO eStudentDTO) {
        Session session = sessionFactory.getCurrentSession();
        EStudent eStudent = new EStudent();
        ReflectionUtil.copyProperties(eStudentDTO, eStudent);
        session.delete(eStudent);
        session.flush();
    }
}
