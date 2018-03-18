package com.extjs.service.impl;

import com.extjs.dao.EQuestionMaximumDao;
import com.extjs.dao.EpaperQTypeDao;
import com.extjs.model.EPaperQType;
import com.extjs.model.EPaperQTypeDTO;
import com.extjs.model.EQuestionMaximum;
import com.extjs.service.EQuestionMaximumService;
import com.extjs.service.EpaperQTypeService;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Scope("prototype")
public class EQuestionMaximumServiceImpl implements EQuestionMaximumService {
    @Autowired
    EQuestionMaximumDao eQuestionMaximumDao;
    EpaperQTypeDao epaperQTypeDao;

    @Override
    public EQuestionMaximum getQuestionMaximum(String id) {
        EQuestionMaximum eQuestionMaximum = eQuestionMaximumDao.getQuestionMaximum(id);
        return eQuestionMaximum;
    }

    @Override
    public List<EQuestionMaximum> getQuestionMaximum(EQuestionMaximum questionMaximum) {
        List<EQuestionMaximum> eQuestionMaximums = eQuestionMaximumDao.getQuestionMaximum(questionMaximum);
        return eQuestionMaximums;
    }

    @Override
    public void modifQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException {
        if (null == questionMaximum.getId() || "".equals(questionMaximum.getId())) {
            questionMaximum.setId(UUID.randomUUID().toString());
        }
        eQuestionMaximumDao.modifQuestionMaximum(questionMaximum);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void addMaximums(String paperId, String questionTypeId, Integer questionNum, Float maximum) throws SysException {


        EQuestionMaximum questionMaximum = new EQuestionMaximum();

        questionMaximum.setPaperId(paperId);
        questionMaximum.setQuestionTypeId(questionTypeId);
        eQuestionMaximumDao.delQuestionMaximum(questionMaximum);
        questionMaximum.setMaximum(maximum);
        for (int i = 0; i < questionNum; i++) {
            questionMaximum.setId(UUID.randomUUID().toString());
            questionMaximum.setQuestionIndex(i + 1);
            this.modifQuestionMaximum(questionMaximum);

        }
    }

//    @Override
//    public void addQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException {
//        eQuestionMaximumDao.addQuestionMaximum(questionMaximum);
//    }
//
//    @Override
//    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
//    public void updateQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException {
//        EPaperQTypeDTO paperQTypeDTO = new EPaperQTypeDTO();
//        paperQTypeDTO.setTpno(questionMaximum.getPaperId());
//        paperQTypeDTO.setQuestiontype(questionMaximum.getQuestionTypeId());
//        List<EPaperQType> paperQTypeS = epaperQTypeDao.queryEpaperQType(paperQTypeDTO);
//        EPaperQType paperQType = new EPaperQType();
//        for (EPaperQType ePaperQType : paperQTypeS) {
//            paperQType = ePaperQType;//主表中的唯一数据
//        }
//        Float sumMaximum = eQuestionMaximumDao.getSumMaximum(questionMaximum.getPaperId(), questionMaximum.getQuestionTypeId());
//        paperQType.setMark(sumMaximum);
//        ReflectionUtil.copyProperties(paperQType,paperQTypeDTO);
//        epaperQTypeDao.modifEpaperQType(paperQTypeDTO);
//        eQuestionMaximumDao.updateQuestionMaximum(questionMaximum);
//    }

    @Override
    public void delQuestionMaximum(EQuestionMaximum questionMaximum) throws SysException {
        eQuestionMaximumDao.delQuestionMaximum(questionMaximum);
    }
}
