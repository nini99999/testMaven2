package com.extjs.dao;

import com.extjs.model.EPaperQuestions;
import com.extjs.model.VPaperQuestionAndInfo;

import java.util.List;

/**
 * Created by jenny on 2017/6/22.
 */
public interface EpaperQuestionsDao {
    List<EPaperQuestions> getPaperQuestions(EPaperQuestions ePaperQuestions);//查询试卷试题

    void addPaperQuestions(EPaperQuestions ePaperQuestions);//添加试卷试题

    EPaperQuestions updatePaperQuestions(EPaperQuestions ePaperQuestions);//修改试卷试题

    void delPaperQuestions(EPaperQuestions ePaperQuestions);//根据id或questionid删除试卷试题

    void mergePaperQuestion(EPaperQuestions paperQuestions);//修改

    List<VPaperQuestionAndInfo> getPaperQuestionAndInfo(VPaperQuestionAndInfo vPaperQuestionAndInfo);//查询试卷试题及信息表
}
