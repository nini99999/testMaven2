package com.extjs.dao.impl;

import com.extjs.dao.EKnowledgeDao;
import com.extjs.model.EKnowledge;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Scope("prototype")
@Repository
public class EKowledgeDaoImpl implements EKnowledgeDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<EKnowledge> queryKnowledge(EKnowledge knowledge) {
        StringBuilder stringBuilder = new StringBuilder("from EKnowledge where 1=1");
        if (null != knowledge.getId() && knowledge.getId().length() > 0) {
            stringBuilder.append(" and id='" + knowledge.getId() + "'");
        } else {
            if (null != knowledge.getParentid() && knowledge.getParentid().length() > 0) {
                stringBuilder.append(" and parentid='" + knowledge.getParentid() + "'");
            }
            if (null != knowledge.getSubjectno() && knowledge.getSubjectno().length() > 0) {
                stringBuilder.append(" and subjectno='" + knowledge.getSubjectno() + "'");
            }
            if (null != knowledge.getGradeno() && knowledge.getGradeno().length() > 0) {
                stringBuilder.append(" and gradeno='" + knowledge.getGradeno() + "'");
            }
            if (null != knowledge.getKnowledgeText() && knowledge.getKnowledgeText().length() > 0) {
                stringBuilder.append(" and knowledgeText='" + knowledge.getKnowledgeText() + "'");
            }
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(stringBuilder.toString());
        List<EKnowledge> knowledges = query.list();
        return knowledges;
    }

    private List<EKnowledge> getTree(String sql) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(sql)
                .addScalar("ID", StringType.INSTANCE)
                .addScalar("PARENTID", StringType.INSTANCE)
                .addScalar("SUBJECTNO", StringType.INSTANCE)
                .addScalar("HASCHILD", IntegerType.INSTANCE)
                .addScalar("KNOWLEDGE", StringType.INSTANCE);
        List list = query.list();
        List<EKnowledge> knowledges = new ArrayList<>();
        EKnowledge knowledge = new EKnowledge();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            knowledge = new EKnowledge();
            Object[] objects = (Object[]) iterator.next();
            knowledge.setId(objects[0].toString());
            knowledge.setParentid(objects[1].toString());
            knowledge.setSubjectno(objects[2].toString());
            knowledge.setHaschild(Integer.parseInt(objects[3].toString()));
            knowledge.setKnowledgeText(objects[4].toString());
            //查询当前节点的下一级子节点（OR 所有子节点？）
//            EKnowledge eKnowledge = new EKnowledge();
//            eKnowledge.setParentid(knowledge.getId());
//            List<EKnowledge> eKnowledges = this.queryKnowledge(eKnowledge);
//            knowledge.setChildren(eKnowledges);
            knowledges.add(knowledge);
        }

        return knowledges;
    }

    @Override
    public List<EKnowledge> getKnowledgeContainsChilds(String id) {
        StringBuilder stringBuilder = new StringBuilder(
                "select ID,PARENTID,GRADENO,SUBJECTNO,HASCHILD,KNOWLEDGE from E_KNOWLEDGE start with id='")
                .append(id).append("'  connect by prior id=parentid order siblings by KNOWLEDGE");

        List<EKnowledge> knowledges = this.getTree(stringBuilder.toString());

        return knowledges;
    }

    @Override
    public List<EKnowledge> getKnowledgeChilds(String id) {
        StringBuilder stringBuilder = new StringBuilder(
                "select ID,PARENTID,GRADENO,SUBJECTNO,HASCHILD,KNOWLEDGE from E_KNOWLEDGE start with parentid='")
                .append(id).append("'  connect by prior id=parentid order siblings by KNOWLEDGE desc");

        List<EKnowledge> knowledges = this.getTree(stringBuilder.toString());

        return knowledges;
    }

    @Override
    public void addKnowledge(EKnowledge knowledge) throws SysException {
        knowledge.setHaschild(0);//添加记录默认设置没有子节点
        Session session = sessionFactory.getCurrentSession();
        session.save(knowledge);
        session.flush();
    }

    @Override
    public void modifKnowledge(EKnowledge knowledge) throws SysException {
        Session session = sessionFactory.getCurrentSession();
        session.merge(knowledge);
        session.flush();
    }

    @Override
    public void saveKnowledgeTextOnly(String id, String knowledgeText) throws SysException {
        String hql = "update EKnowledge set knowledgeText=:knowledgeText where id=:id";
        Session session = sessionFactory.getCurrentSession();
        if (null != id && id.length() > 0) {
            session.createQuery(hql).setString("knowledgeText", knowledgeText).setString("id", id).executeUpdate();
            session.flush();
        }

    }

    @Override
    public void delKnowledge(String id) throws SysException {
        if (null != id && id.length() > 0) {
            String hql = "delete from EKnowledge where id='" + id + "'";
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(hql);
            query.executeUpdate();
            session.flush();
        } else {
        }
    }

    @Override
    public EKnowledge getKnowledgeByID(String id) {
        EKnowledge knowledge = new EKnowledge();
        StringBuilder stringBuilder = new StringBuilder("from EKnowledge where 1=1");
        if (null != id && id.length() > 0) {
            stringBuilder.append(" and id='" + id + "'");
            Session session = sessionFactory.getCurrentSession();
            knowledge = (EKnowledge) session.createQuery(stringBuilder.toString()).uniqueResult();
        }


        return knowledge;
    }
}
