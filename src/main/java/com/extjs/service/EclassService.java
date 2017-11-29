package com.extjs.service;


import com.extjs.model.EClassDTO;
import com.extjs.util.SysException;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jenny on 2017/3/22.
 */
public interface EclassService {
    List<EClassDTO> queryEclass();
    HashMap<String,EClassDTO> queryEclassMap();
    List<EClassDTO> queryEclassByDTO(EClassDTO eClassDTO);
    String addEclass(EClassDTO eClassDTO) throws SysException;
    void delEclass(EClassDTO eClassDTO) throws SysException;
}
