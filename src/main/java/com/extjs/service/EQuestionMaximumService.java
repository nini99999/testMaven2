package com.extjs.service;

import com.extjs.model.EQuestionMaximum;
import com.extjs.util.SysException;

import java.util.List;

public interface EQuestionMaximumService {
    EQuestionMaximum getQuestionMaximum(String id);

    List<EQuestionMaximum> getQuestionMaximum(EQuestionMaximum questionMaximum);

    void modifQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException;

    void addMaximums(String paperId, String questionTypeId, Integer questionNum, Float maximum) throws SysException;

// 方法废弃，相关操作改为由数据库表触发器执行
// void addQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException;
//
//    /**
//     * 更新时同步更新主表E_Paper_Q_Type中的mark字段=sum（字表maximum），步骤1、查询主表记录，2更新mark，添加事物管理
//     * @param questionMaximum
//     * @throws SysException
//     */
//    void updateQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException;

    void delQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException;
}
