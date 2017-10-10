package com.extjs.dao;

import com.extjs.model.Role;
import com.extjs.model.RoleDTO;

import java.util.List;

/**
 * Created by Administrator on 2016/2/4.
 */
public interface RoleDao {
    List<Role> getRoles(String[]ids);
    String addRole(RoleDTO roleDTO);
}
