package com.extjs.service;

import com.extjs.model.ESubjectQTypeDTO;
import com.extjs.util.SysException;

import java.util.List;

/**
 * Created by jenny on 2017/4/2.
 */
public interface EsubjectQTypeService {
    List<ESubjectQTypeDTO> queryESubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO);

    String addESubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO) throws SysException;

    void addESubjectQT(List<ESubjectQTypeDTO> eSubjectQTypeDTOList) throws SysException;

    void delESubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO) throws SysException;

    void delESubjectQT(String subjectno) throws SysException;

}
