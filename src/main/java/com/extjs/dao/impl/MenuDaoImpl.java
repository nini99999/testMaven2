package com.extjs.dao.impl;

import com.extjs.dao.MenuDao;
import com.extjs.model.Menu;
import com.extjs.model.MenuDTO;
import com.extjs.model.RoleMenuKey;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2015/12/24.
 */
@Repository
@Scope("prototype")
public class MenuDaoImpl implements MenuDao {
    private static Log logger= LogFactory.getLog(MenuDaoImpl.class);
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Menu> selectChildrenMenu(String menuId,String[] menuIds) {
        Session session = sessionFactory.getCurrentSession();
        String hql="from Menu where parentMenuId='"+menuId+"'";
        if (menuId==null){
            hql="from Menu where parentMenuId is null";
        }
        if (menuIds!=null&&menuIds.length>0){
            hql="from Menu where parentMenuId='"+menuId+"' and menuId in (:menuIds)";
            Query query = session.createQuery(hql);
            query.setParameterList("menuIds",menuIds);
            List<Menu> menus = query.list();
            return menus;
        }
        Query query = session.createQuery(hql);
        List<Menu> menus = query.list();
        return menus;
    }

    @Override
    public void addMenu(MenuDTO menuDTO) {
        Session session = sessionFactory.getCurrentSession();
        Menu menu=new Menu();
        ReflectionUtil.copyProperties(menuDTO,menu);
        session.save(menu);
        session.flush();
    }

    @Override
    public void delMenu(String menuId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete from Menu where menuId='" + menuId + "'");
        query.executeUpdate();
    }

    @Override
    public List<Menu> selectMenus(String[] menuIds) {
        Session session = sessionFactory.getCurrentSession();
        String hql="";
        if (menuIds!=null&&menuIds.length>0){
            hql="from Menu where menuId in (:menuIds) and parentMenuId is null";
        }
        Query query = session.createQuery(hql);
        query.setParameterList("menuIds",menuIds);
        List<Menu> menus = query.list();
        return menus;
    }
}
