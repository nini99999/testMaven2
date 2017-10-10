package com.extjs.dao;

import com.extjs.model.ETestpaper;
import com.extjs.model.ETestpaperDTO;

import java.util.List;

/**
 * Created by jenny on 2017/4/4.
 */
public interface EtestpaperDao {
    List<ETestpaper> queryEtestPaper(ETestpaperDTO eTestpaperDTO);//查询试卷

    ETestpaper getTestPaper(String tpno, String tpname);//根据试卷id或试卷名称查询唯一记录

    Integer getSumQuestionNum(String tpno);//根据试卷id查询指定试卷的题目总数

    void modifEtestPaper(ETestpaperDTO eTestpaperDTO);//添加、修改试卷

    void delEtestPaper(ETestpaperDTO eTestpaperDTO);//删除试卷
}
