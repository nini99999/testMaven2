package com.extjs.dao.impl;

import com.extjs.dao.RoleDao;
import com.extjs.model.Role;
import com.extjs.model.RoleDTO;
import com.extjs.util.ReflectionUtil;
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
public class RoleDaoimpl implements RoleDao{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Role> getRoles(String[]ids) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Role where roleId in (:ids)");
        query.setParameterList("ids",ids);
        List<Role> roles = query.list();
        return roles;
    }

    @Override
    public String addRole(RoleDTO roleDTO) {
        Session session = sessionFactory.getCurrentSession();
        Role role=new Role();
        ReflectionUtil.copyProperties(roleDTO,role);
        String pk=session.save(role)+"";
        return pk;
    }
}
