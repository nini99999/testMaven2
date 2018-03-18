package com.extjs.service.impl;

import com.extjs.dao.ESubjectQTypeDao;
import com.extjs.dao.EpaperQTypeDao;
import com.extjs.dao.EtestpaperDao;
import com.extjs.model.*;
import com.extjs.service.EpaperQTypeService;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

/**
 * Created by jenny on 2017/4/6.
 */
@Service
@Scope("prototype")
public class EpaperQTypeServiceImpl implements EpaperQTypeService {
    @Autowired
    private EpaperQTypeDao epaperQTypeDao;
    @Autowired
    private ESubjectQTypeDao eSubjectQTypeDao;

    @Override
    public List<EPaperQTypeDTO> queryEpaperQType(EPaperQTypeDTO ePaperQTypeDTO) {
        List<EPaperQTypeDTO> ePaperQTypeDTOList = new ArrayList<EPaperQTypeDTO>();
        List<EPaperQType> ePaperQTypeList = epaperQTypeDao.queryEpaperQType(ePaperQTypeDTO);
        HashMap<String, String> qtypeMap = this.getSubjectQTypeMap();
        for (EPaperQType ePaperQType : ePaperQTypeList) {
            ePaperQTypeDTO = new EPaperQTypeDTO();
            ReflectionUtil.copyProperties(ePaperQType, ePaperQTypeDTO);
            ePaperQTypeDTO.setQuestiontypename(qtypeMap.get(ePaperQTypeDTO.getQuestiontype()));
            ePaperQTypeDTOList.add(ePaperQTypeDTO);
        }
        return ePaperQTypeDTOList;
    }

    /**
     * 获取题型名称Map
     *
     * @return
     */

    public HashMap<String, String> getSubjectQTypeMap() {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        ESubjectQTypeDTO eSubjectQTypeDTO = new ESubjectQTypeDTO();
        List<ESubjectQType> eSubjectQTypeList = eSubjectQTypeDao.queryESubjectQT(eSubjectQTypeDTO);
        for (ESubjectQType eSubjectQType : eSubjectQTypeList) {
            resultMap.put(eSubjectQType.getQuestiontype(), eSubjectQType.getQuestiontypename());
        }
        return resultMap;
    }

    @Override
    public void addEpaperQType(EPaperQTypeDTO ePaperQTypeDTO) throws SysException {
        if (null == ePaperQTypeDTO.getId()) {
            UUID uuid = UUID.randomUUID();
            ePaperQTypeDTO.setId(uuid.toString());
        }
        if (null == ePaperQTypeDTO.getQuestionnum()) {
            ePaperQTypeDTO.setQuestionnum(0);
        }
        if (null == ePaperQTypeDTO.getMark()) {
            ePaperQTypeDTO.setMark(0.0f);
        }
        Date date = new Date(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        ePaperQTypeDTO.setCreatedate(date);
        ePaperQTypeDTO.setCreator(userDetails.getUsername());
        epaperQTypeDao.modifEpaperQType(ePaperQTypeDTO);

    }

    @Override
    public void addEpaperQTypeList(List<EPaperQTypeDTO> ePaperQTypeDTOList) throws SysException {
        for (EPaperQTypeDTO ePaperQTypeDTO : ePaperQTypeDTOList) {
            this.addEpaperQType(ePaperQTypeDTO);
        }
    }

    @Override
    public void delEpaperQType(String tpno) throws SysException {
        epaperQTypeDao.delEpaperQType(tpno);
    }

    @Override
    public void delEpaperQType(List<EPaperQTypeDTO> ePaperQTypeDTOList) throws SysException {
        for (EPaperQTypeDTO ePaperQTypeDTO : ePaperQTypeDTOList) {
            this.delEpaperQType(ePaperQTypeDTO.getTpno());
        }
    }
}
