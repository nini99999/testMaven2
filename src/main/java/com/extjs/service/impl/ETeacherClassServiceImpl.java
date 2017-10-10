package com.extjs.service.impl;

import com.extjs.dao.EteacherClassDao;
import com.extjs.model.ETeacherClass;
import com.extjs.model.ETeacherClassDTO;
import com.extjs.service.EclassService;
import com.extjs.service.EteacherClassService;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
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
 * Created by jenny on 2017/3/28.
 */
@Service
@Scope("prototype")
public class ETeacherClassServiceImpl implements EteacherClassService {
    @Autowired
    private EteacherClassDao eteacherClassDao;
    @Autowired
    private EclassService eclassService;

    @Override
    public List<ETeacherClassDTO> queryETeacherClass(ETeacherClassDTO eTeacherClassDTO) {
        List<ETeacherClassDTO> eTeacherClassDTOList = new ArrayList<ETeacherClassDTO>();
        List<ETeacherClass> eTeacherClassList = eteacherClassDao.queryETeacherClass(eTeacherClassDTO);
        HashMap<String, String> classMap = eclassService.queryEclassMap();
        for (ETeacherClass eTeacherClass : eTeacherClassList) {
            eTeacherClassDTO = new ETeacherClassDTO();
            ReflectionUtil.copyProperties(eTeacherClass, eTeacherClassDTO);
            eTeacherClassDTO.setClassname(classMap.get(eTeacherClassDTO.getClassno()));
            eTeacherClassDTOList.add(eTeacherClassDTO);
        }
        return eTeacherClassDTOList;
    }

    @Override
    public String addETeacherClass(ETeacherClassDTO eTeacherClassDTO) throws SysException {
        UUID uuid = UUID.randomUUID();
        Date date = new Date(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        eTeacherClassDTO.setId(uuid.toString());
        eTeacherClassDTO.setCreatedate(date);
        eTeacherClassDTO.setCreator(userDetails.getUsername());
        String flag = eteacherClassDao.addETeacherClass(eTeacherClassDTO) + "";
        return flag;
    }

    @Override
    public void delETeacherClass(ETeacherClassDTO eTeacherClassDTO) throws SysException {
        eteacherClassDao.delETeacherClass(eTeacherClassDTO);
    }

    public void delETeacherClassByTeacherid(String teacherid) throws SysException {
        eteacherClassDao.delETeacherClassByTeacherID(teacherid);
    }

    @Override
    public void addETeacherClass(List<ETeacherClassDTO> eTeacherClassDTOList) throws SysException {
        for (ETeacherClassDTO eTeacherClassDTO : eTeacherClassDTOList) {
            this.addETeacherClass(eTeacherClassDTO);
        }
    }
}
