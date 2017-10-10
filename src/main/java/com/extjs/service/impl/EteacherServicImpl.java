package com.extjs.service.impl;

import com.extjs.dao.EteacherDao;
import com.extjs.model.ETeacher;
import com.extjs.model.ETeacherDTO;
import com.extjs.service.EteacherService;
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

/**
 * Created by jenny on 2017/3/27.
 */
@Service
@Scope("prototype")
public class EteacherServicImpl implements EteacherService {
    @Autowired
    private EteacherDao eteacherDao;

    @Override
    public List<ETeacherDTO> queryEteacher(ETeacherDTO eTeacherDTO) {
        List<ETeacherDTO> eTeacherDTOList = new ArrayList<ETeacherDTO>();
        List<ETeacher> eTeacherList = eteacherDao.queryEteacher(eTeacherDTO);

        for (ETeacher eTeacher : eTeacherList) {
            eTeacherDTO = new ETeacherDTO();
            ReflectionUtil.copyProperties(eTeacher, eTeacherDTO);
            eTeacherDTOList.add(eTeacherDTO);
        }

        return eTeacherDTOList;
    }

    @Override
    public String addEteacher(ETeacherDTO eTeacherDTO) throws SysException {
        UUID uuid = UUID.randomUUID();
        Date date = new Date(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        eTeacherDTO.setId(uuid.toString());
        eTeacherDTO.setCreator(userDetails.getUsername());
        eTeacherDTO.setCreatedate(date);
        eTeacherDTO.setTeacherid(eTeacherDTO.getSchoolno()+"-"+eTeacherDTO.getTeacherid());
        String flag = eteacherDao.addEteacher(eTeacherDTO);
        return flag;
    }

    @Override
    public void delEteacher(ETeacherDTO eTeacherDTO) throws SysException {
        eteacherDao.delEteacher(eTeacherDTO);
    }
}
