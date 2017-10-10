package com.extjs.dao.impl;

import com.extjs.dao.UserDao;
import com.extjs.model.Role;
import com.extjs.model.RoleDTO;
import com.extjs.model.User;
import com.extjs.model.UserDTO;
import com.extjs.util.ReflectionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/4.
 */
@Repository
@Scope("prototype")
public class UserDaoImpl implements UserDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<User> queryUserList() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User");
        List<User> users = query.list();
        return users;
    }

    @Override
    public List<Role> queryRoleList() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Role");
        List<Role> roles = query.list();
        return roles;
    }

    @Override
    public String addUser(UserDTO userDTO) {
        Session session = sessionFactory.getCurrentSession();
        User user = new User();
        ReflectionUtil.copyProperties(userDTO, user);
//        session.saveOrUpdate(user);
        String flag = session.save(user) + "";
        session.flush();
        return flag;
    }

    @Override
    public List<User> getUserList(String userName) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User where userName='" + userName + "'");
        List<User> users = query.list();
        return users;
    }

    @Override
    public User getUserByUnique(String userName) {
        Session session = sessionFactory.getCurrentSession();
        User user = (User) session.createQuery("from User where userName='" + userName + "'").uniqueResult();
//        User user=query.uniqueResult();
        return user;
    }
}
