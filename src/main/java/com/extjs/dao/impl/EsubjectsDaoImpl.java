package com.extjs.dao.impl;


import com.extjs.dao.EsubjectsDao;
import com.extjs.model.Esubjects;
import com.extjs.model.EsubjectsDTO;
import com.extjs.util.ReflectionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jenny on 2017/3/16.
 */

@Repository
@Scope("prototype")
public class EsubjectsDaoImpl implements EsubjectsDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Esubjects> queryEsubjectsList() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Esubjects");
        List<Esubjects> esubjects = query.list();
        return esubjects;
    }



    @Override
    public String addEsubjects(EsubjectsDTO esubjectsDTO) {
        Session session = sessionFactory.getCurrentSession();
        Esubjects esubjects = new Esubjects();
        ReflectionUtil.copyProperties(esubjectsDTO, esubjects);
        String flag = session.save(esubjects) + "";
        session.flush();
        return flag;
    }

    @Override
    public void delEsbujects(EsubjectsDTO esubjectsDTO) {
        Session session = sessionFactory.getCurrentSession();
        Esubjects esubjects = new Esubjects();
        ReflectionUtil.copyProperties(esubjectsDTO, esubjects);
        session.delete(esubjects);
        session.flush();
    }
}
