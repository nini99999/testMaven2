package com.extjs.service;

import com.extjs.model.ETeacherClassDTO;
import com.extjs.util.SysException;

import java.util.List;

/**
 * Created by jenny on 2017/3/28.
 */
public interface EteacherClassService {
    List<ETeacherClassDTO> queryETeacherClass(ETeacherClassDTO eTeacherClassDTO);

    String addETeacherClass(ETeacherClassDTO eTeacherClassDTO) throws SysException;

    void delETeacherClass(ETeacherClassDTO eTeacherClassDTO) throws SysException;

    void delETeacherClassByTeacherid(String teacherid) throws SysException;

    void addETeacherClass(List<ETeacherClassDTO> eTeacherClassDTOList) throws SysException;//添加多条选中记录
}
