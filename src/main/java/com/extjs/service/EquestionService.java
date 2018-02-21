package com.extjs.service;

import com.extjs.model.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by jenny on 2017/6/23.
 */
public interface EquestionService {
//    List<EQuestionsDTO> getQuestionList(EQuestionsDTO eQuestionsDTO);//获取试题集合
    List<VQuestionandinfo> getQuestionAndInfoList(VQuestionandinfo questionandinfo,Page page);//根据视图查询试题及试题信息

    String getOneQuestion(String questionID);//获取一道试题题干,返回html格式文本

    EQuestionsDTO getOneQuestionDTO(String uid);//获取一道试题，返回DTO

    void addOneQuestion(EQuestionsDTO eQuestionsDTO);//插入一道题目(基础库)

    void delOneQuestion(EQuestionsDTO eQuestionsDTO);//删除一道题目by questionID

    void updateOneQuestionAndInfo(EQuestionsDTO eQuestionsDTO,EQuestionInfoDTO eQuestionInfoDTO);//根据题目id修改一道题目信息，先执行删除，然后执行添加操作

    void addOneQuestionInfo(EQuestionInfoDTO eQuestionInfoDTO);//插入一道题目的相关信息

//    void addOnePaperQuestion(EPaperQuestionsDTO ePaperQuestionsDTO);//插入一条试卷题目信息

    /**
     * //插入试题及信息记录
     * @param uQuestionID 如果是修改，则记录删除题目的questionID，并保持修改后的questionID不变；如果新增，则自动生成
     * @param question
     * @param gradeNo
     * @param subjectNo
     * @param questionType
     * @param difficulty
     */

    void addOneQuestionAndInfo(String uQuestionID,String question,String gradeNo,String subjectNo,String questionType,Float difficulty);

    String exportToHTML(HttpServletResponse response , VQuestionandinfo questionandinfo,String url);//导出至html，返回导出路径+html文件名

    void addQuestionAndInfoList(List<String> list,String gradeno,String subjectno,String questionType,Float difficulty);

    int getTotalCount(VQuestionandinfo questionandinfo);//查询指定条件的总记录数
}
