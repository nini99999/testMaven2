package com.extjs.dao.impl;

import com.extjs.dao.EgradeDao;
import com.extjs.model.EGrade;
import com.extjs.model.EGradeDTO;
import com.extjs.util.ReflectionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jenny on 2017/3/20.
 */
@Repository
@Scope("prototype")


public class EgradeDaoImpl implements EgradeDao {

    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public EGrade queryEgradeByID(String gradeno) {
        Session session = sessionFactory.getCurrentSession();
        EGrade eGrade=(EGrade) session.createQuery("from EGrade where gradeno='"+gradeno+"'").uniqueResult();
        return eGrade;
    }

    @Override
    public List<EGrade> queryEgrade() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from EGrade");
        List<EGrade> eGrades=query.list();
        return eGrades;
    }

    public List<EGrade> queryEgradeByschoolno(String schoolno) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from EGrade where schoolno='" + schoolno + "'");
        List<EGrade> eGrades=query.list();
        return eGrades;

    }

    @Override
    public String addEgrade(EGradeDTO eGradeDTO) {
        Session session = sessionFactory.getCurrentSession();
       EGrade eGrade=new EGrade();
        ReflectionUtil.copyProperties(eGradeDTO,eGrade);
        String flag=session.save(eGrade)+"";
        session.flush();
        return flag;
    }

    @Override
    public void delEgrade(EGradeDTO eGradeDTO) {
        Session session = sessionFactory.getCurrentSession();
        EGrade eGrade=new EGrade();
        ReflectionUtil.copyProperties(eGradeDTO,eGrade);
        session.delete(eGrade);
        session.flush();
    }
}
