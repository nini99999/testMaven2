package com.extjs.dao.impl;

import com.extjs.dao.EteacherClassDao;
import com.extjs.model.ETeacherClass;
import com.extjs.model.ETeacherClassDTO;
import com.extjs.model.VTeacherClass;
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
 * Created by jenny on 2017/3/28.
 */
@Repository
@Scope("prototype")
public class EteacherClassDaoImpl implements EteacherClassDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<ETeacherClass> queryETeacherClass(ETeacherClassDTO eTeacherClassDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("from ETeacherClass");
        if (null != eTeacherClassDTO.getTeacherno() && !"".equals(eTeacherClassDTO.getTeacherno())) {
            sb.append(" where teacherno='" + eTeacherClassDTO.getTeacherno() + "'");
        }

        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        List<ETeacherClass> eTeacherClassList = query.list();
        return eTeacherClassList;
    }

    @Override
    public VTeacherClass getTeacherClass(VTeacherClass teacherClass) {

        StringBuilder stringBuilder = new StringBuilder(" from VTeacherClass where 1=1");
        if (null != teacherClass.getClassno() && teacherClass.getClassno().length() > 0) {
            stringBuilder.append(" and classno='" + teacherClass.getClassno() + "'");
        }
        if (null != teacherClass.getSubjectno() && teacherClass.getSubjectno().length() > 0) {
            stringBuilder.append(" and subjectno='" + teacherClass.getSubjectno() + "'");
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(stringBuilder.toString());
        List<VTeacherClass> teacherClassList = query.list();
        VTeacherClass result = new VTeacherClass();
        String teacherName = "";
        for (VTeacherClass vTeacherClass : teacherClassList) {
            ReflectionUtil.copyProperties(vTeacherClass, result);
            if (null != vTeacherClass.getTeachername() && vTeacherClass.getTeachername().length() > 0) {
                teacherName = vTeacherClass.getTeachername() + "," + teacherName;
            }

        }
        result.setTeachername(teacherName);
        return result;
    }

    @Override
    public String addETeacherClass(ETeacherClassDTO eTeacherClassDTO) {
        Session session = sessionFactory.getCurrentSession();
        ETeacherClass eTeacherClass = new ETeacherClass();
        ReflectionUtil.copyProperties(eTeacherClassDTO, eTeacherClass);
        String flag = session.save(eTeacherClass) + "";
        session.flush();
        return flag;
    }

    @Override
    public void delETeacherClass(ETeacherClassDTO eTeacherClassDTO) {
        Session session = sessionFactory.getCurrentSession();
        ETeacherClass eTeacherClass = new ETeacherClass();
        ReflectionUtil.copyProperties(eTeacherClassDTO, eTeacherClass);
        session.delete(eTeacherClass);
        session.flush();
    }

    public void delETeacherClassByTeacherID(String teacherid) {
        String delHQL = "delete from ETeacherClass where teacherno='" + teacherid + "'";
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(delHQL).executeUpdate();
        session.flush();
    }
}
