package com.extjs.dao.impl;

import com.extjs.dao.ESubjectQTypeDao;
import com.extjs.model.ESubjectQType;
import com.extjs.model.ESubjectQTypeDTO;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Scope("prototype")
/**
 * Created by jenny on 2017/4/2.
 */
public class ESubjectQTypeDaoImpl implements ESubjectQTypeDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<ESubjectQType> queryESubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("from ESubjectQType");
        if (null != eSubjectQTypeDTO.getSubjectno() && !"".equals(eSubjectQTypeDTO.getSubjectno())) {
            sb.append(" where subjectno='" + eSubjectQTypeDTO.getSubjectno() + "'");
        }
        sb.append(" order by subjectno,questiontype");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        List<ESubjectQType> eSubjectQTypeList = query.list();
        return eSubjectQTypeList;
    }

    @Override
    public String addESubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO) throws SysException {
        Session session = sessionFactory.getCurrentSession();
        ESubjectQType eSubjectQType = new ESubjectQType();
        ReflectionUtil.copyProperties(eSubjectQTypeDTO, eSubjectQType);
        session.saveOrUpdate(eSubjectQType);

        session.flush();
        return "success";
    }

    @Override
    public void delESubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO) throws SysException {
        Session session = sessionFactory.getCurrentSession();
        ESubjectQType eSubjectQType = new ESubjectQType();
        ReflectionUtil.copyProperties(eSubjectQTypeDTO, eSubjectQType);
        session.delete(eSubjectQType);
        session.flush();
    }

    @Override
    public void delESubjectQTByID(String subjectno) throws SysException {
        String delHql = "delete from ESubjectQType where subjectno='" + subjectno + "'";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(delHql).executeUpdate();
        session.flush();
    }
}
