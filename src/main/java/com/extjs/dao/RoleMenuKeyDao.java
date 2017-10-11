package com.extjs.dao;

import com.extjs.model.RoleMenuKey;

import java.util.List;

/**
 * Created by Administrator on 2016/2/4.
 */
public interface RoleMenuKeyDao {
    void addRoleMenuKey(RoleMenuKey roleMenuKey);
    List<RoleMenuKey> getRoleMenuKeys(String[] roleIds);
    void deleteRoleMenuKey(String menuId);
    void deleteRoleMenuKeyByRoleId(String roleId);
}
