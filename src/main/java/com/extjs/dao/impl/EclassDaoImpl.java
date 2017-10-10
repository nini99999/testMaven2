package com.extjs.dao.impl;

import com.extjs.dao.EclassDao;
import com.extjs.model.EClass;
import com.extjs.model.EClassDTO;
import com.extjs.util.ReflectionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jenny on 2017/3/22.
 */
@Repository
@Scope("prototype")
public class EclassDaoImpl implements EclassDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override

    public List<EClass> queryEclass() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from EClass");
        List<EClass> eclassList = query.list();
        return eclassList;
    }

    @Override
    public List<EClass> queryEclass(EClassDTO eClassDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("from EClass where 1=1");
        if (null != eClassDTO.getSchoolno() && !"".equals(eClassDTO.getSchoolno())) {
            sb.append(" and schoolno='" + eClassDTO.getSchoolno() + "'");
        }
        if (null != eClassDTO.getGradeno() && !"".equals(eClassDTO.getGradeno())) {
            sb.append(" and gradeno='" + eClassDTO.getGradeno() + "'");
        }

        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        List<EClass> eclassList = query.list();
        return eclassList;
    }

    @Override
    public String addEclass(EClassDTO eClassDTO) {
        Session session = sessionFactory.getCurrentSession();
        EClass eClass = new EClass();
        ReflectionUtil.copyProperties(eClassDTO, eClass);
        String flag = session.save(eClass) + "";
        session.flush();
        return flag;
    }

    @Override
    public void delEclass(EClassDTO eClassDTO) {
        Session session = sessionFactory.getCurrentSession();
        EClass eClass = new EClass();
        ReflectionUtil.copyProperties(eClassDTO, eClass);
        session.delete(eClass);
        session.flush();

    }
}
