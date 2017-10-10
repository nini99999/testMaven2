package com.extjs.service;

import com.extjs.model.EWrongStudentDTO;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jenny on 2017/5/22.
 */
public interface EwrongStudentService {

    List<EWrongStudentDTO> queryWrongStudent(EWrongStudentDTO eWrongStudentDTO);

    HashMap<Integer, Integer> getQuestionno(String countryid);

    void saveOrUpdateWrongStudent(EWrongStudentDTO eWrongStudentDTO);

    void delWrongStudent(EWrongStudentDTO eWrongStudentDTO);

    /**
     * 根据页面选择的错题号集合，先删除指定学籍号、试卷名称的错题，然后再循环添加选中错题
     * 待处理：知识点尚未添加至表
     * @param eWrongStudentDTOList
     * @param countryid
     * @param testpapername
     * @return
     */
    String modifdSelected(List<EWrongStudentDTO> eWrongStudentDTOList);
}
