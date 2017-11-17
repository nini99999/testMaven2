package com.extjs.service.impl;

import com.extjs.dao.EstudentDao;
import com.extjs.model.ESchoolDTO;
import com.extjs.model.EStudentDTO;
import com.extjs.model.EStudent;
import com.extjs.service.EschoolService;
import com.extjs.service.EstudentService;
import com.extjs.service.UserService;
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
    EschoolService eschoolService;

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

    @Override
    public String addEstudent(EStudentDTO eStudentDTO) throws SysException {
        UUID uuid = UUID.randomUUID();
        Date date = new Date(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        eStudentDTO.setId(uuid.toString());
        if (null == eStudentDTO.getSchoolno() || eStudentDTO.getSchoolno().length() == 0) {
            String schoolid = userService.getUserByUnique(userDetails.getUsername()).getuserSchool();
            ESchoolDTO schoolDTO = eschoolService.querySchoolByUnique(schoolid, null);
            eStudentDTO.setSchoolno(schoolDTO.getSchoolno());
        }
        eStudentDTO.setCreator(userDetails.getUsername());
        eStudentDTO.setCreatedate(date);
        String flag = estudentDao.addEstudent(eStudentDTO);
        return flag;
    }

    @Override
    public void delEstudent(EStudentDTO eStudentDTO) throws SysException {
        estudentDao.delEstudent(eStudentDTO);
    }
}
