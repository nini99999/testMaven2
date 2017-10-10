package com.extjs.service;

import com.extjs.model.*;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
/**
 * Created by jenny on 2017/6/23.
 */
public interface EquestionService {
    List<EQuestionsDTO> getQuestionList(EQuestionsDTO eQuestionsDTO);//获取试题集合
    List<VQuestionandinfo> getQuestionAndInfoList(VQuestionandinfo questionandinfo);//根据视图查询试题及试题信息

    String getOneQuestion(String questionID);//获取一道试题题干,返回html格式文本

    EQuestionsDTO getOneQuestionDTO(String uid);//获取一道试题，返回DTO

    void addOneQuestion(EQuestionsDTO eQuestionsDTO);//插入一道题目(基础库)

    void updateOneQuestionAndInfo(EQuestionsDTO eQuestionsDTO,EQuestionInfoDTO eQuestionInfoDTO);//根据题目id修改一道题目信息，先执行删除，然后执行添加操作

    void addOneQuestionInfo(EQuestionInfoDTO eQuestionInfoDTO);//插入一道题目的相关信息

    void addOnePaperQuestion(EPaperQuestionsDTO ePaperQuestionsDTO);//插入一条试卷题目信息

    void addOneQuestionAndInfo(String question,String gradeNo,String subjectNo,String questionType,Float difficulty);

    String  exportToHTML(HttpServletResponse response ,VQuestionandinfo questionandinfo);//导出至html，返回+html文件名


}
