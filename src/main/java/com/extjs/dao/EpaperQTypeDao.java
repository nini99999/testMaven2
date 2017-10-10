package com.extjs.dao;

import com.extjs.model.EPaperQType;
import com.extjs.model.EPaperQTypeDTO;

import java.util.List;

/**
 * Created by jenny on 2017/4/4.
 */
public interface EpaperQTypeDao {
    List<EPaperQType> queryEpaperQType(EPaperQTypeDTO ePaperQTypeDTO);//查询试卷题型表

    void modifEpaperQType(EPaperQTypeDTO ePaperQTypeDTO);//添加、维护试卷题型

    void delEpaperQType( String tpno);//删除试卷题型
}
