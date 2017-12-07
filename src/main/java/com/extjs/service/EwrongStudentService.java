package com.extjs.service;

import com.extjs.model.EWrongStudent;
import com.extjs.model.EWrongStudentDTO;
import com.extjs.model.RWrongQuestion;
import com.extjs.model.VPaperQuestionAndInfo;
import com.extjs.util.SysException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jenny on 2017/5/22.
 */
public interface EwrongStudentService {

    List<EWrongStudentDTO> queryWrongStudent(EWrongStudentDTO eWrongStudentDTO);

    HashMap<Integer, Integer> getQuestionno(String studentID);//获取指定学生已存在的错题

    /**
     * //获取指定学生已存在的错题
     *
     * @param studentID
     * @return hashMap:questionID,id
     */
    HashMap<String, String> getWrongByStudent(String studentID);

    /**
     * 查询指定试卷的试题信息集合，结合试卷信息表、学生信息表封装成学生错题集合，并标注该集合中每条数据的state（是否选中，即是否已登记错题）
     *
     * @param paperid
     * @param studentid
     * @return
     */
    List<EWrongStudentDTO> getQuestionListWithState(String paperid, String studentid, String currentUser);

    /**
     * 根据考试日期，获取指定日期内的错题信息
     * @param beginDate
     * @param endDate
     * @return
     */
    List<RWrongQuestion> getWrongsByDateArea(String beginDate, String endDate,String subjectno,String gradeno);

    void saveOrUpdateWrongStudent(EWrongStudentDTO eWrongStudentDTO);

    void delWrongStudent(EWrongStudentDTO eWrongStudentDTO);

    /**
     * 根据页面选择的错题号集合，先删除指定学籍号、试卷名称的错题，然后再循环添加选中错题
     * 待处理：知识点尚未添加至表
     *
     * @param eWrongStudentDTOList
     * @param countryid
     * @param testpapername
     * @return
     */
    String modifdSelected(List<EWrongStudentDTO> eWrongStudentDTOList);

    /**
     * 根据学生和试卷id，查询错题，返回题目集合
     *
     * @param studentid
     * @param paperid
     * @return
     */
    List<VPaperQuestionAndInfo> getQuestionsByWrong(String studentid, String paperid,String classno);

    /**
     * 按查询条件导出至html
     *
     * @param response
     * @param studentid
     * @param subjectno
     * @param gradeno
     * @param classno
     * @param paperid
     * @return
     * @throws SysException
     */
    String exportHTML(HttpServletResponse response, String studentid, String classno, String paperid,String url) throws SysException;
}
