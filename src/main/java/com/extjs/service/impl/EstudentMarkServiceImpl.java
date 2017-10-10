package com.extjs.service.impl;

import com.extjs.dao.EStudentMarkDao;
import com.extjs.model.EStudentMark;
import com.extjs.service.EstudentMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

/**
 * Created by jenny on 2017/5/12.
 */
@Service
@Scope("prototype")
public class EstudentMarkServiceImpl implements EstudentMarkService {
    @Autowired
    private EStudentMarkDao studentMarkDao;



    @Override
    public List<EStudentMark> queryEStudentMark(EStudentMark eStudentMark) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Date date = new Date(System.currentTimeMillis());
        eStudentMark.setCreator(userDetails.getUsername());
        eStudentMark.setCreatedate(date);
        List<EStudentMark> studentMarks = studentMarkDao.queryEStudentMark(eStudentMark);
        return studentMarks;
    }

    @Override
    public void modifEStudentMark(EStudentMark eStudentMark) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Date date = new Date(System.currentTimeMillis());
        eStudentMark.setCreator(userDetails.getUsername());
        eStudentMark.setCreatedate(date);
        studentMarkDao.saveOrUpdateEStudentMark(eStudentMark);
    }

    @Override
    public void delEStudentMark(EStudentMark eStudentMark) {

    }

    @Override
    public void delEStudentMark() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
//        Date date = new Date(System.currentTimeMillis());
        studentMarkDao.delEStudentMarkByCreator(userDetails.getUsername());
    }
}
