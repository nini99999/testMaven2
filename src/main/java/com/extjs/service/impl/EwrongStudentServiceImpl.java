package com.extjs.service.impl;

import com.extjs.dao.EstudentDao;
import com.extjs.dao.EtestpaperDao;
import com.extjs.dao.EwrongStudentDao;
import com.extjs.model.*;
import com.extjs.service.EwrongStudentService;
import com.extjs.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by jenny on 2017/5/22.
 */
@Service
@Scope("prototype")
public class EwrongStudentServiceImpl implements EwrongStudentService {
    @Autowired
    private EwrongStudentDao ewrongStudentDao;
    @Autowired
    private EstudentDao estudentDao;
    @Autowired
    private EtestpaperDao etestpaperDao;

    @Override
    public List<EWrongStudentDTO> queryWrongStudent(EWrongStudentDTO eWrongStudentDTO) {

        List<EWrongStudent> eWrongStudents = ewrongStudentDao.queryEWrongStudent(eWrongStudentDTO);

        List<EWrongStudentDTO> eWrongStudentDTOList = new ArrayList<EWrongStudentDTO>();
        for (EWrongStudent eWrongStudent : eWrongStudents) {
            eWrongStudentDTO = new EWrongStudentDTO();
            ReflectionUtil.copyProperties(eWrongStudent, eWrongStudentDTO);
            if (null != eWrongStudent.getTestpaperno() && eWrongStudent.getTestpaperno().length() > 0) {//试卷名赋值
                ETestpaper eTestpaper = etestpaperDao.getTestPaper(eWrongStudent.getTestpaperno(), null);
                eWrongStudentDTO.setTestpapername(eTestpaper.getTpname());
            }

            if (null != eWrongStudent.getId() && eWrongStudent.getId().length() > 0) {//学生姓名赋值
                String studentname = estudentDao.getStudentByID(eWrongStudent.getStudentid()).getStudentname();
                eWrongStudentDTO.setStudentname(studentname);
            } else {
                if (null != eWrongStudent.getCountryid() && eWrongStudent.getCountryid().length() > 0) {//学生姓名赋值
                    String studentname = estudentDao.getEstudentByCountryID(eWrongStudent.getCountryid()).getStudentname();
                    eWrongStudentDTO.setStudentname(studentname);
                }
            }
            eWrongStudentDTOList.add(eWrongStudentDTO);
        }
        return eWrongStudentDTOList;
    }

    @Override
    public HashMap<Integer, Integer> getQuestionno(String studentID) {
        HashMap<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
        if (null != studentID && studentID.length() > 0) {
            EWrongStudentDTO eWrongStudentDTO = new EWrongStudentDTO();
//            eWrongStudentDTO.setCountryid(countryid);
            eWrongStudentDTO.setStudentid(studentID);
            List<EWrongStudent> eWrongStudents = ewrongStudentDao.queryEWrongStudent(eWrongStudentDTO);
            for (EWrongStudent eWrongStudent : eWrongStudents) {
                resultMap.put(eWrongStudent.getQuestionno(), eWrongStudent.getQuestionno());
            }
        }
        return resultMap;
    }

    @Override
    public void saveOrUpdateWrongStudent(EWrongStudentDTO eWrongStudentDTO) {

        Date date = new Date(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        eWrongStudentDTO.setCreator(userDetails.getUsername());
        eWrongStudentDTO.setCreatedate(date);
        if (null == eWrongStudentDTO.getId() || "".equals(eWrongStudentDTO.getId())) {
            UUID uuid = UUID.randomUUID();
            eWrongStudentDTO.setId(uuid.toString());
        }
        try {

            EWrongStudent eWrongStudent = new EWrongStudent();
            ReflectionUtil.copyProperties(eWrongStudentDTO, eWrongStudent);
//            EStudentDTO eStudentDTO=new EStudentDTO();
//            eStudentDTO.setId(eWrongStudentDTO.getStudentid());
            EStudent eStudent = estudentDao.getStudentByID(eWrongStudentDTO.getStudentid());//获得学生姓名并赋值
//            EStudent eStudent = estudentDao.getEstudentByCountryID(eWrongStudentDTO.getCountryid());//获得学生姓名并赋值
//            eWrongStudent.setStudentname(eStudent.getStudentname());
            eWrongStudent.setCountryid(eStudent.getCountryid());
            ewrongStudentDao.addEWrongStudent(eWrongStudent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delWrongStudent(EWrongStudentDTO eWrongStudentDTO) {
        try {
            EWrongStudent eWrongStudent = new EWrongStudent();
            ReflectionUtil.copyProperties(eWrongStudentDTO, eWrongStudent);
            ewrongStudentDao.delEWrongStudent(eWrongStudent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String modifdSelected(List<EWrongStudentDTO> eWrongStudentDTOList) {
        String res = "success";
        try {
            for (EWrongStudentDTO eWrongStudentDTO : eWrongStudentDTOList) {

                this.saveOrUpdateWrongStudent(eWrongStudentDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = e.toString();
        }

        return res;
    }
}
