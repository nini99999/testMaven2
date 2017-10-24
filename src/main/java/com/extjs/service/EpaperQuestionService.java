package com.extjs.service;

import com.extjs.model.EPaperQuestionsDTO;
import com.extjs.model.ETestpaperDTO;
import com.extjs.model.VPaperQuestionAndInfo;

import java.util.List;

/**
 * Created by jenny on 2017/10/14.
 */
public interface EpaperQuestionService {
    void addOnePaperQuestion(EPaperQuestionsDTO ePaperQuestionsDTO);//插入一条试卷题目信息

    void delPaperQuestion(EPaperQuestionsDTO ePaperQuestionsDTO);//删除试卷题目（id或试卷id）

    List<EPaperQuestionsDTO> queryPaperQuestions(EPaperQuestionsDTO ePaperQuestionsDTO);//查询

    void mergePaperQuestion(EPaperQuestionsDTO ePaperQuestionsDTO);//修改

    void addPaperQuestionAndInfo(VPaperQuestionAndInfo paperQuestionAndInfo);//添加试卷试题表和试题信息表

    void mergePaperQuestionAndInfo(VPaperQuestionAndInfo vPaperQuestionAndInfo);//修改试卷试题表和试题信息表

    List<VPaperQuestionAndInfo> getPaperQuestionAndInfo(VPaperQuestionAndInfo vPaperQuestionAndInfo);//查询试卷试题表和试题信息表

    List<ETestpaperDTO> getTestPaperListByTimeInterval(VPaperQuestionAndInfo vPaperQuestionAndInfo);//根据时间区间查询试卷编号和名称
}
