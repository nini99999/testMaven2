package com.extjs.service;


import com.extjs.model.ETestpaperDTO;
import com.extjs.util.SysException;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jenny on 2017/4/4.
 */
public interface EtestpaperService {
    List<ETestpaperDTO> queryEtestpaper(ETestpaperDTO eTestpaperDTO);

    ETestpaperDTO getTestPaperByTPNO(String tpno);//根据试卷编码查询试卷

    Integer getSumQuestionNum(String tpno);

    void addEtestpaper(ETestpaperDTO eTestpaperDTO) throws SysException;

    HashMap<String, ETestpaperDTO> getEtestPaper();//获取所有试卷编号和名称

    void delEtestpaper(ETestpaperDTO eTestpaperDTO) throws SysException;


}
