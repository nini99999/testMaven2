package com.extjs.dao;

import com.extjs.model.EQuestionInfo;
import com.extjs.model.VQuestionandinfo;

import java.util.List;

/**
 * Created by jenny on 2017/6/22.
 */
public interface EquestionInfoDao {
    List<EQuestionInfo> getQuestionInfo(EQuestionInfo eQuestionInfo);//查询题目信息

    void saveOrUpdateQuestionInfo(EQuestionInfo eQuestionInfo);//修改or添加题目信息

    void delQuestionInfo(String questionid);//根据试题id删除题目信息

    List<VQuestionandinfo> getQuestionAndInfos(VQuestionandinfo questionandinfo);
}
