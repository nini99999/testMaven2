package com.extjs.service.impl;

import com.extjs.dao.RoleDao;
import com.extjs.dao.RoleMenuKeyDao;
import com.extjs.dao.UserDao;
import com.extjs.dao.UserRoleKeyDao;
import com.extjs.model.*;
import com.extjs.service.UserService;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/4.
 */
@Service
@Scope("prototype")
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleKeyDao userRoleKeyDao;
    @Autowired
    private RoleMenuKeyDao roleMenuKeyDao;

    @Override
    public List<User> queryUserList() {
        return userDao.queryUserList();
    }

    @Override
    public List<RoleDTO> queryRoleList() {
        RoleDTO roleDTO = null;

        List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
        List<Role> roles = userDao.queryRoleList();
        for (Role role : roles) {
            roleDTO = new RoleDTO();
            String menuIds = "";
            ReflectionUtil.copyProperties(role, roleDTO);
            List<RoleMenuKey> roleMenuKeys = roleMenuKeyDao.getRoleMenuKeys(new String[]{role.getRoleId()});
            for (RoleMenuKey roleMenuKey : roleMenuKeys) {
                menuIds += roleMenuKey.getMenuId() + ",";
            }
            if (!"".equals(menuIds)) {
                roleDTO.setMenuIds(menuIds.substring(0, menuIds.length() - 1));
            }
            roleDTOs.add(roleDTO);
        }
        return roleDTOs;
    }


    /**
     * 两个插入操作加入事务同时回滚
     *
     * @param userDTO
     * @throws SysException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void addUser(UserDTO userDTO) throws SysException {
        String userRoleIds = userDTO.getUserRoleIds();
        String[] ids = null;
        if (userRoleIds != null && !"".equals(userRoleIds)) {
            ids = userRoleIds.split(",");
        }
        List<Role> roles = roleDao.getRoles(ids);
        String userRoleNames = "";
        for (Role role : roles) {
            userRoleNames += role.getRoleName() + ",";
        }
        userDTO.setUserRoleNames(userRoleNames.substring(0, userRoleNames.length() - 1));
        String sid = userDao.addUser(userDTO);

        UserRoleKey userRoleKey = null;
        for (String id : ids) {
            userRoleKey = new UserRoleKey();
            userRoleKey.setUserId(sid);
            userRoleKey.setRoleId(id);
            userRoleKeyDao.addUserRole(userRoleKey);
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void deleteUser(String userId) throws SysException {
        userRoleKeyDao.deleteUserRole(userId);
        userDao.deleteUser(userId);
    }

    @Override
    public List<UserDTO> getUser(String userName) throws SysException {
        List<UserDTO> userDTOList = new ArrayList<UserDTO>();
        List<User> userList = userDao.getUserList(userName);
        UserDTO userDTO = null;
        for (User user : userList) {
            userDTO = new UserDTO();
            ReflectionUtil.copyProperties(user, userDTO);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    @Override
    public UserDTO getUserByUnique(String userName) throws SysException {
        User user = userDao.getUserByUnique(userName);
        UserDTO userDTO = new UserDTO();
        ReflectionUtil.copyProperties(user, userDTO);
        return userDTO;
    }
}
