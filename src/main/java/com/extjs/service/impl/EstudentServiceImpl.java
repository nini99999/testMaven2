package com.extjs.service.impl;

import com.extjs.dao.EstudentDao;
import com.extjs.model.*;
import com.extjs.service.EschoolService;
import com.extjs.service.EstudentService;
import com.extjs.service.RoleService;
import com.extjs.service.UserService;
import com.extjs.util.DESUtil;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.extjs.util.EConstants;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jenny on 2017/3/24.
 */
@Service
@Scope("prototype")
public class EstudentServiceImpl implements EstudentService {
    @Autowired
    private EstudentDao estudentDao;
    @Autowired
    private UserService userService;
    @Autowired
    private EschoolService eschoolService;
    @Autowired
    private RoleService roleService;

    @Override
    public List<EStudentDTO> queryEstudent(EStudentDTO eStudentDTO) {

        List<EStudentDTO> eStudentDTOList = new ArrayList<EStudentDTO>();
        List<EStudent> eStudentList = estudentDao.queryEstudent(eStudentDTO);
        EConstants eConstants = new EConstants();

        for (EStudent eStudent : eStudentList) {
            eStudentDTO = new EStudentDTO();
            ReflectionUtil.copyProperties(eStudent, eStudentDTO);
            eStudentDTO.setStudystate(eConstants.studyStateMap.get(eStudentDTO.getStudystate()));
            eStudentDTO.setSchoolstate(eConstants.schoolStateMap.get(eStudentDTO.getSchoolstate()));

            eStudentDTOList.add(eStudentDTO);
        }

        return eStudentDTOList;
    }

    @Override
    public List<EStudentDTO> getStudentByClassAndTpno(String classno, String tpno) {
        List<EStudentDTO> eStudentDTOList = new ArrayList<EStudentDTO>();
        List<EStudent> eStudentList = estudentDao.queryEstudentByClassAndTpno(classno, tpno);
        EConstants eConstants = new EConstants();
        for (EStudent eStudent : eStudentList) {
            EStudentDTO eStudentDTO = new EStudentDTO();
            ReflectionUtil.copyProperties(eStudent, eStudentDTO);
            eStudentDTO.setStudystate(eConstants.studyStateMap.get(eStudentDTO.getStudystate()));
            eStudentDTO.setSchoolstate(eConstants.schoolStateMap.get(eStudentDTO.getSchoolstate()));

            eStudentDTOList.add(eStudentDTO);
        }
        return eStudentDTOList;
    }

    @Override
    public EStudentDTO getStudentByID(String id) {

        EStudentDTO studentDTO = new EStudentDTO();
        if (null != id && id.length() > 0) {
            EStudent student = estudentDao.getStudentByID(id);
            ReflectionUtil.copyProperties(student, studentDTO);
            EConstants eConstants = new EConstants();
            studentDTO.setStudystate(eConstants.studyStateMap.get(studentDTO.getStudystate()));
            studentDTO.setSchoolstate(eConstants.schoolStateMap.get(studentDTO.getSchoolstate()));
        }

        return studentDTO;
    }

    /**
     * 添加学生时，同步添加User表
     * @param eStudentDTO
     * @return
     * @throws SysException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public String addEstudent(EStudentDTO eStudentDTO) throws SysException {
        UUID uuid = UUID.randomUUID();
        Date date = new Date(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        eStudentDTO.setId(uuid.toString());
        String schoolid = userService.getUserByUnique(userDetails.getUsername()).getuserSchool();
        if (null == eStudentDTO.getSchoolno() || eStudentDTO.getSchoolno().length() == 0) {
            ESchoolDTO schoolDTO = eschoolService.querySchoolByUnique(schoolid, null);
            eStudentDTO.setSchoolno(schoolDTO.getSchoolno());
        }
        eStudentDTO.setCreator(userDetails.getUsername());
        eStudentDTO.setCreatedate(date);
        UserDTO userDTO = new UserDTO();
//        userDTO.setCreateTime(String.valueOf(date));
        userDTO.setUserDescription(eStudentDTO.getStudentname());
        userDTO.setUserGrade(eStudentDTO.getGradeno());

        userDTO.setuserSchool(schoolid);
        userDTO.setUserId(uuid.toString());
        userDTO.setUserName(eStudentDTO.getUsername());
        userDTO.setUserPassword(DESUtil.md5(EConstants.defaultPassWord));
        userDTO.setUserRealName(eStudentDTO.getStudentname());
        /*********获取用户角色ID及名称*********/
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName(EConstants.defaultStudentRole);
        RoleDTO roleDTO1 = roleService.getRole(roleDTO);
        /***************end*****************/
        userDTO.setUserRoleIds(roleDTO1.getRoleId());//用户所属角色
//        userDTO.setUserRoleNames(roleDTO1.getRoleName());//用户所属角色名称
        userService.addUser(userDTO);
        return estudentDao.addEstudent(eStudentDTO);
    }

    @Override
    public void delEstudent(EStudentDTO eStudentDTO) throws SysException {
        estudentDao.delEstudent(eStudentDTO);
    }
}
