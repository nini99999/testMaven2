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
public class RoleDaoimpl implements RoleDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Role> getRoles(String[] ids) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Role where roleId in (:ids)");
        query.setParameterList("ids", ids);
        List<Role> roles = query.list();
        return roles;
    }

    @Override
    public Role getRole(RoleDTO roleDTO) {
        StringBuilder stringBuilder = new StringBuilder("from Role where 1=1");
        if (null != roleDTO.getRoleId() && roleDTO.getRoleId().length() > 0) {
            stringBuilder.append(" and roleId='" + roleDTO.getRoleId() + "'");
        }
        if (null != roleDTO.getRoleName() && roleDTO.getRoleName().length() > 0) {
            stringBuilder.append(" and roleName='" + roleDTO.getRoleName() + "'");
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(stringBuilder.toString());
        Role role = (Role) query.uniqueResult();
        return role;
    }

    @Override
    public String addRole(RoleDTO roleDTO) {
        Session session = sessionFactory.getCurrentSession();
        Role role = new Role();
        ReflectionUtil.copyProperties(roleDTO, role);
        String pk = session.save(role) + "";
        return pk;
    }

    @Override
    public String saveRole(RoleDTO roleDTO) {
        Session session = sessionFactory.getCurrentSession();
        Role role = new Role();
        ReflectionUtil.copyProperties(roleDTO, role);
        session.saveOrUpdate(role);
        return roleDTO.getRoleId();
    }

    @Override
    public void deleteRole(String roleId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete from Role where roleId='" + roleId + "'");
        query.executeUpdate();
    }
}
