package com.extjs.dao.impl;

import com.extjs.dao.EteacherDao;
import com.extjs.model.ETeacher;
import com.extjs.model.ETeacherDTO;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jenny on 2017/3/26.
 */
@Repository
@Scope("prototype")
public class EteacherDaoImpl implements EteacherDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<ETeacher> queryEteacher(ETeacherDTO eTeacherDTO) {
        StringBuilder sb = new StringBuilder("");
        sb.append("from ETeacher where 1=1");
        if (null != eTeacherDTO.getSchoolno() && !"".equals(eTeacherDTO.getSchoolno())) {
            sb.append(" and schoolno='" + eTeacherDTO.getSchoolno() + "'");
        }
        if (null != eTeacherDTO.getSubjectno() && !"".equals(eTeacherDTO.getSubjectno())) {
            sb.append(" and subjectno='" + eTeacherDTO.getSubjectno() + "'");
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());

        List<ETeacher> eTeacherList = query.list();
        return eTeacherList;
    }

    @Override
    public String addEteacher(ETeacherDTO eTeacherDTO) throws SysException {
        Session session = sessionFactory.getCurrentSession();
        ETeacher eTeacher = new ETeacher();
        ReflectionUtil.copyProperties(eTeacherDTO, eTeacher);
        String flag = session.save(eTeacher) + "";
        session.flush();
        return flag;
    }

    @Override
    public void delEteacher(ETeacherDTO eTeacherDTO) throws SysException {
        Session session = sessionFactory.getCurrentSession();
        ETeacher eTeacher = new ETeacher();
        ReflectionUtil.copyProperties(eTeacherDTO, eTeacher);
        session.delete(eTeacher);
        session.flush();
    }
}
