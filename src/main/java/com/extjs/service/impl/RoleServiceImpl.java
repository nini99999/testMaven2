package com.extjs.service.impl;

import com.extjs.dao.RoleDao;
import com.extjs.dao.RoleMenuKeyDao;
import com.extjs.model.Role;
import com.extjs.model.RoleDTO;
import com.extjs.model.RoleMenuKey;
import com.extjs.service.RoleService;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/2/16.
 */
@Service
@Scope("prototype")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleMenuKeyDao roleMenuKeyDao;


    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void addRole(RoleDTO roleDTO) throws SysException {
        String pk = roleDao.addRole(roleDTO);
        if (pk != null && !"".equals(pk)) {
            String menuIds = roleDTO.getMenuIds();
            String[] menus = menuIds.split(",");
            RoleMenuKey roleMenuKey = null;
            for (String menu : menus) {
                roleMenuKey = new RoleMenuKey();
                roleMenuKey.setRoleId(pk);
                roleMenuKey.setMenuId(menu);
                roleMenuKeyDao.addRoleMenuKey(roleMenuKey);
            }
        }

    }

    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void saveRole(RoleDTO roleDTO) throws SysException {
        String pk = roleDao.saveRole(roleDTO);
        roleMenuKeyDao.deleteRoleMenuKeyByRoleId(pk);
        if (pk != null && !"".equals(pk)) {
            String menuIds = roleDTO.getMenuIds();
            String[] menus = menuIds.split(",");
            RoleMenuKey roleMenuKey = null;
            for (String menu : menus) {
                roleMenuKey = new RoleMenuKey();
                roleMenuKey.setRoleId(pk);
                roleMenuKey.setMenuId(menu);
                roleMenuKeyDao.addRoleMenuKey(roleMenuKey);
            }
        }
    }

    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void deleteRole(String roleId) throws SysException {
        roleMenuKeyDao.deleteRoleMenuKeyByRoleId(roleId);
        roleDao.deleteRole(roleId);
    }

    @Override
    public RoleDTO getRole(RoleDTO roleDTO) throws SysException {
        Role role = roleDao.getRole(roleDTO);
        RoleDTO res = new RoleDTO();
        ReflectionUtil.copyProperties(role, res);
        return res;
    }


}
