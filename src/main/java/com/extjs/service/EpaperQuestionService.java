package com.extjs.service;

import com.extjs.model.EPaperQTypeDTO;
import com.extjs.model.EPaperQuestionsDTO;
import com.extjs.model.ETestpaperDTO;
import com.extjs.model.VPaperQuestionAndInfo;
import com.extjs.util.SysException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by jenny on 2017/10/14.
 */
public interface EpaperQuestionService {
    void addOnePaperQuestion(EPaperQuestionsDTO ePaperQuestionsDTO);//插入一条试卷题目信息

    void delPaperQuestion(EPaperQuestionsDTO ePaperQuestionsDTO);//删除试卷题目（id或试卷id）

    List<EPaperQuestionsDTO> queryPaperQuestions(EPaperQuestionsDTO ePaperQuestionsDTO);//查询

    void mergePaperQuestion(EPaperQuestionsDTO ePaperQuestionsDTO);//修改

    /**
     *
     * @param uQuestionID 如果是删除后调用该方法（修改），保持原题目的questionID不变；新增时自动生成
     * @param paperQuestionAndInfo
     */
    void addPaperQuestionAndInfo(String uQuestionID,VPaperQuestionAndInfo paperQuestionAndInfo);//添加试卷试题表和试题信息表,同步增加至试题基础库

    /**
     * 循环指定集合，插入至试卷试题表、试题信息表和试题答案表
     * @param questionTypeList
     * @param paperQTypeDTOList
     * @param paperid
     * @param gradeno
     * @param subjectno
     * @throws SysException
     */
    void addPaperQuestionAndInfoList(List<List<String>> questionTypeList, List<EPaperQTypeDTO> paperQTypeDTOList, String paperid, String gradeno, String subjectno) throws SysException;

    void mergePaperQuestionAndInfo(VPaperQuestionAndInfo vPaperQuestionAndInfo);//修改试卷试题表和试题信息表

    List<VPaperQuestionAndInfo> getPaperQuestionAndInfo(VPaperQuestionAndInfo vPaperQuestionAndInfo);//查询试卷试题表和试题信息表

    List<ETestpaperDTO> getTestPaperListByTimeInterval(VPaperQuestionAndInfo vPaperQuestionAndInfo);//根据时间区间查询试卷编号和名称

    String exportHTML(HttpServletResponse response, VPaperQuestionAndInfo paperQuestionAndInfo, String url);//导出HTML
}
