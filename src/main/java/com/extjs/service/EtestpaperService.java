package com.extjs.service;


import com.extjs.model.ETestpaperDTO;
import com.extjs.util.SysException;

import java.util.List;

/**
 * Created by jenny on 2017/4/4.
 */
public interface EtestpaperService {
    List<ETestpaperDTO> queryEtestpaper(ETestpaperDTO eTestpaperDTO);

    Integer getSumQuestionNum(String tpno);

    void addEtestpaper(ETestpaperDTO eTestpaperDTO) throws SysException;



    void delEtestpaper(ETestpaperDTO eTestpaperDTO) throws SysException;


}
