package com.extjs.dao.impl;

import com.extjs.dao.RoleMenuKeyDao;
import com.extjs.model.RoleMenuKey;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/2/4.
 */
@Repository
@Scope("prototype")
public class RoleMenuKeyDaoImpl implements RoleMenuKeyDao{
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public void addRoleMenuKey(RoleMenuKey roleMenuKey) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(roleMenuKey);

    }

    @Override
    public List<RoleMenuKey> getRoleMenuKeys(String[] roleIds) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from RoleMenuKey where roleId in (:roleIds)");
        query.setParameterList("roleIds",roleIds);
        List<RoleMenuKey> roleMenuKeys = query.list();
        return roleMenuKeys;
    }

    @Override
    public void deleteRoleMenuKey(String menuId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete from RoleMenuKey where menuId='" + menuId + "'");
        query.executeUpdate();
    }

    @Override
    public void deleteRoleMenuKeyByRoleId(String roleId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete from RoleMenuKey where roleId='" + roleId + "'");
        query.executeUpdate();
    }
}
