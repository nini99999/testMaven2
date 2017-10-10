package com.extjs.service;

import com.extjs.model.EPaperQTypeDTO;
import com.extjs.util.SysException;

import java.util.List;

/**
 * Created by jenny on 2017/4/6.
 */
public interface EpaperQTypeService {
    List<EPaperQTypeDTO> queryEpaperQType(EPaperQTypeDTO ePaperQTypeDTO);

    void addEpaperQType(EPaperQTypeDTO ePaperQTypeDTO) throws SysException;

    void addEpaperQTypeList(List<EPaperQTypeDTO> ePaperQTypeDTOList) throws SysException;

    void delEpaperQType(String tpno) throws SysException;

    void delEpaperQType(List<EPaperQTypeDTO> ePaperQTypeDTOList) throws SysException;
}
