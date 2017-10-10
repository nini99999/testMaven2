package com.extjs.service;

import com.extjs.model.ETeacherDTO;
import com.extjs.util.SysException;

import java.util.List;

/**
 * Created by jenny on 2017/3/26.
 */
public interface EteacherService {
    List<ETeacherDTO> queryEteacher(ETeacherDTO eTeacherDTO);

    String addEteacher(ETeacherDTO eTeacherDTO) throws SysException;

    void delEteacher(ETeacherDTO eTeacherDTO) throws SysException;
}
