package com.extjs.dao.impl;

import com.extjs.dao.EschoolDao;
import com.extjs.model.ESchool;
import com.extjs.model.ESchoolDTO;
import com.extjs.util.ReflectionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jenny on 2017/3/18.
 */
@Repository
@Scope("prototype")
public class EschoolDaoImpl implements EschoolDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<ESchool> queryEschool() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from ESchool");
        List<ESchool> eSchools = query.list();
        return eSchools;
    }

    @Override
    public String addEschool(ESchoolDTO eSchoolDTO) {
        Session session = sessionFactory.getCurrentSession();
        ESchool eSchool = new ESchool();
        ReflectionUtil.copyProperties(eSchoolDTO, eSchool);
        String flag = session.save(eSchool) + "";
        session.flush();
        return flag;
    }

    @Override
    public void delEschool(ESchoolDTO eSchoolDTO) {
        Session session = sessionFactory.getCurrentSession();
        ESchool eSchool = new ESchool();
        ReflectionUtil.copyProperties(eSchoolDTO, eSchool);
        session.delete(eSchool);
        session.flush();

    }

    public ESchool queryEschool(ESchool eSchool) {
        StringBuilder sb = new StringBuilder();
        sb.append("from ESchool where 1=1");
        if (null != eSchool.getId() && !"".equals(eSchool.getId())) {
            sb.append(" and id='" + eSchool.getId() + "'");
        }
        if (null != eSchool.getSchoolno() && !"".equals(eSchool.getSchoolno())) {
            sb.append(" and schoolno='" + eSchool.getSchoolno() + "'");
        }
        Session session = sessionFactory.getCurrentSession();

        ESchool eSchool1 = (ESchool) session.createQuery(sb.toString()).uniqueResult();
        return eSchool1;
    }
}
