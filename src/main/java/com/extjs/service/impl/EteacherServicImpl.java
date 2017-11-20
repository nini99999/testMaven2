package com.extjs.service.impl;

import com.extjs.dao.EteacherDao;
import com.extjs.model.*;
import com.extjs.service.EschoolService;
import com.extjs.service.EteacherService;
import com.extjs.service.RoleService;
import com.extjs.service.UserService;
import com.extjs.util.DESUtil;
import com.extjs.util.EConstants;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jenny on 2017/3/27.
 */
@Service
@Scope("prototype")
public class EteacherServicImpl implements EteacherService {
    @Autowired
    private EteacherDao eteacherDao;
    @Autowired
    private EschoolService eschoolService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    public List<ETeacherDTO> queryEteacher(ETeacherDTO eTeacherDTO) {
        List<ETeacherDTO> eTeacherDTOList = new ArrayList<ETeacherDTO>();
        if (null == eTeacherDTO.getSchoolno() || eTeacherDTO.getSchoolno().length() > 0) {
            try {
                eTeacherDTO.setSchoolno(eschoolService.getSchoolnoByContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<ETeacher> eTeacherList = eteacherDao.queryEteacher(eTeacherDTO);

        for (ETeacher eTeacher : eTeacherList) {
            eTeacherDTO = new ETeacherDTO();
            ReflectionUtil.copyProperties(eTeacher, eTeacherDTO);
            eTeacherDTOList.add(eTeacherDTO);
        }

        return eTeacherDTOList;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public String addEteacher(ETeacherDTO eTeacherDTO) throws SysException {
        UUID uuid = UUID.randomUUID();
        Date date = new Date(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String schoolid = userService.getUserByUnique(userDetails.getUsername()).getuserSchool();
        if (null == eTeacherDTO.getSchoolno() || eTeacherDTO.getSchoolno().length() == 0) {
            eTeacherDTO.setSchoolno(eschoolService.getSchoolnoByContext());
        }
        eTeacherDTO.setId(uuid.toString());
        eTeacherDTO.setCreator(userDetails.getUsername());
        eTeacherDTO.setCreatedate(date);
        eTeacherDTO.setTeacherid(eTeacherDTO.getSchoolno() + "-" + eTeacherDTO.getTeacherid());
        UserDTO userDTO = new UserDTO();
//        userDTO.setCreateTime(String.valueOf(date));
        userDTO.setUserDescription(eTeacherDTO.getTeachername());

        userDTO.setuserSchool(schoolid);
        userDTO.setUserId(uuid.toString());
        userDTO.setUserName(String.valueOf(eTeacherDTO.getTel()));//教师默认登陆账号为其手机号
        userDTO.setUserPassword(DESUtil.md5(EConstants.defaultPassWord));
        userDTO.setUserRealName(eTeacherDTO.getTeachername());
        /*********获取用户角色ID及名称*********/
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName(EConstants.defaultTeacherRole);
        RoleDTO roleDTO1 = roleService.getRole(roleDTO);
        /***************end*****************/
        userDTO.setUserRoleIds(roleDTO1.getRoleId());//用户所属角色
//        userDTO.setUserRoleNames(roleDTO1.getRoleName());//用户所属角色名称
        userService.addUser(userDTO);
        String flag = eteacherDao.addEteacher(eTeacherDTO);
        return flag;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void delEteacher(ETeacherDTO eTeacherDTO) throws SysException {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(String.valueOf(eTeacherDTO.getTel()));
        userService.deleteUser(userDTO);//同步删除系统用户
        eteacherDao.delEteacher(eTeacherDTO);
    }


}
