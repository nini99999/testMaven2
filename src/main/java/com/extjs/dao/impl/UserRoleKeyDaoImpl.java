package com.extjs.dao.impl;

import com.extjs.dao.UserRoleKeyDao;
import com.extjs.model.UserRoleKey;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/2/4.
 */
@Scope("prototype")
@Repository
public class UserRoleKeyDaoImpl implements UserRoleKeyDao {
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public void addUserRole(UserRoleKey userRoleKey) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(userRoleKey);
        session.flush();
    }

    @Override
    public void deleteUserRole(String userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete from UserRoleKey where userId='" + userId + "'");
        query.executeUpdate();
    }
}
