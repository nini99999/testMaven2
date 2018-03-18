package com.extjs.dao.impl;

import com.extjs.dao.EpaperQTypeDao;
import com.extjs.model.EPaperQType;
import com.extjs.model.EPaperQTypeDTO;
import com.extjs.util.ReflectionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jenny on 2017/4/4.
 */
@Repository
@Scope("prototype")
public class EpaperQTypeDaoImpl implements EpaperQTypeDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<EPaperQType> queryEpaperQType(EPaperQTypeDTO ePaperQTypeDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("from EPaperQType where 1=1");
        if (null != ePaperQTypeDTO.getTpno() && !"".equals(ePaperQTypeDTO.getTpno())) {
            sb.append(" and tpno='" + ePaperQTypeDTO.getTpno() + "'");
        }
        if (null != ePaperQTypeDTO.getQuestiontype() && !"".equals(ePaperQTypeDTO.getQuestiontype())) {
            sb.append(" and questiontype='" + ePaperQTypeDTO.getQuestiontype() + "'");
        }
        sb.append(" order by tpno,questiontype");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        List<EPaperQType> ePaperQTypeList = query.list();
        return ePaperQTypeList;
    }

    @Override
    public void modifEpaperQType(EPaperQTypeDTO ePaperQTypeDTO) {
        Session session = sessionFactory.getCurrentSession();
        if (Float.isNaN(ePaperQTypeDTO.getMark()) || ePaperQTypeDTO.getMark() < 0) {
            ePaperQTypeDTO.setMark(0.0f);
        }
        EPaperQType ePaperQType = new EPaperQType();
        ReflectionUtil.copyProperties(ePaperQTypeDTO, ePaperQType);
        session.merge(ePaperQType);
//        session.saveOrUpdate(ePaperQType);
        session.flush();

    }

    @Override
    public void delEpaperQType(String tpno) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "delete EPaperQType where tpno=:tpno";
        session.createQuery(hql).setString("tpno", tpno).executeUpdate();

        session.flush();
    }
}
