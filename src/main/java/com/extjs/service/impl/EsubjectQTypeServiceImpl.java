package com.extjs.service.impl;

import com.extjs.dao.ESubjectQTypeDao;

import com.extjs.model.ESubjectQType;
import com.extjs.model.ESubjectQTypeDTO;

import com.extjs.service.EsubjectQTypeService;
import com.extjs.service.EsubjectsService;
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
 * Created by jenny on 2017/4/2.
 */
@Service
@Scope("prototype")
public class EsubjectQTypeServiceImpl implements EsubjectQTypeService {
    @Autowired
    private ESubjectQTypeDao eSubjectQTypeDao;
    @Autowired
    private EsubjectsService esubjectsService;

    @Override
    public List<ESubjectQTypeDTO> queryESubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO) {
        List<ESubjectQTypeDTO> eSubjectQTypeDTOList = new ArrayList<ESubjectQTypeDTO>();
        List<ESubjectQType> eSubjectQTypeList = eSubjectQTypeDao.queryESubjectQT(eSubjectQTypeDTO);
        HashMap<String, String> map = esubjectsService.queryEsubjectMap();//获取学科编码、名称map
        for (ESubjectQType eSubjectQType : eSubjectQTypeList) {
            eSubjectQTypeDTO = new ESubjectQTypeDTO();
            ReflectionUtil.copyProperties(eSubjectQType, eSubjectQTypeDTO);
            eSubjectQTypeDTO.setSubjectname(map.get(eSubjectQTypeDTO.getSubjectno()));//根据编码绑定DTO中的名称
            eSubjectQTypeDTOList.add(eSubjectQTypeDTO);
        }

        return eSubjectQTypeDTOList;
    }

    @Override
    public String addESubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO) throws SysException {
        if (null == eSubjectQTypeDTO.getId() || "".equals(eSubjectQTypeDTO.getId())) {
            UUID uuid = UUID.randomUUID();
            eSubjectQTypeDTO.setId(uuid.toString());
        }
        Date date = new Date(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        eSubjectQTypeDTO.setCreator(userDetails.getUsername());
        eSubjectQTypeDTO.setCreatedate(date);
        eSubjectQTypeDao.addESubjectQT(eSubjectQTypeDTO);
        return null;
    }

    @Override
    public void addESubjectQT(List<ESubjectQTypeDTO> eSubjectQTypeDTOList) throws SysException {
        for (ESubjectQTypeDTO eSubjectQTypeDTO : eSubjectQTypeDTOList) {
            this.addESubjectQT(eSubjectQTypeDTO);
        }

    }

    @Override
    public void delESubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO) throws SysException {
        eSubjectQTypeDao.delESubjectQT(eSubjectQTypeDTO);
    }

    @Override
    public void delESubjectQT(String subjectno) throws SysException {
        eSubjectQTypeDao.delESubjectQTByID(subjectno);
    }
}
