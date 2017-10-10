package com.extjs.service;

import com.extjs.model.RoleDTO;
import com.extjs.util.SysException;

/**
 * Created by Administrator on 2016/2/16.
 */
public interface RoleService {
    void addRole(RoleDTO roleDTO)throws SysException;
   // void saveRole(RoleDTO roleDTO)throws SysException;
}
