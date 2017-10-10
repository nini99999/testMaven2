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
        sb.append("from EStudent");
        if (null != eStudentDTO.getSchoolno() && !"".equals(eStudentDTO.getSchoolno())) {
            sb.append(" where schoolno='" + eStudentDTO.getSchoolno() + "'");
        }
        if (null != eStudentDTO.getGradeno() && !"".equals(eStudentDTO.getGradeno())) {
            sb.append(" and gradeno='" + eStudentDTO.getGradeno() + "'");
        }
        if (null != eStudentDTO.getClassno() && !"".equals(eStudentDTO.getClassno())) {
            sb.append(" and classno='" + eStudentDTO.getClassno() + "'");
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
