package com.extjs.service.impl;

import com.extjs.dao.EKnowledgeDao;
import com.extjs.model.EKnowledge;
import com.extjs.model.EQuestionKnowledge;
import com.extjs.model.EWrongKnowledgeDTO;
import com.extjs.service.EKnowledgeService;
import com.extjs.service.EQuestionKnowledgeService;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import com.sun.org.apache.bcel.internal.generic.FLOAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;

@Service
@Scope("prototype")
public class EKnowledgeServiceImpl implements EKnowledgeService {
    @Autowired
    private EKnowledgeDao eKnowledgeDao;

    @Override
    public EKnowledge getKnowledge(String id) {
        EKnowledge eKnowledge = eKnowledgeDao.getKnowledgeByID(id);
        return eKnowledge;
    }

    @Override
    public List<EKnowledge> queryKnowledge(EKnowledge knowledge) {
        List<EKnowledge> eKnowledges = eKnowledgeDao.queryKnowledge(knowledge);
        return eKnowledges;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public EKnowledge addKnowledge(EKnowledge knowledge) throws SysException {
        UUID uuid = UUID.randomUUID();
        if (null == knowledge.getId() || knowledge.getId().length() == 0) {
            knowledge.setId(uuid.toString());
        }

        knowledge.setHaschild(0);//添加时默认设为没有子节点
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        knowledge.setCreator(userDetails.getUsername());
        Date date = new Date(System.currentTimeMillis());
        knowledge.setCreatedate(date);
        EKnowledge eKnowledge = new EKnowledge();
        eKnowledge.setId(knowledge.getParentid());
        List<EKnowledge> knowledges = eKnowledgeDao.queryKnowledge(eKnowledge);
        for (EKnowledge knowledge1 : knowledges) {
            knowledge1.setHaschild(1);
            eKnowledgeDao.modifKnowledge(knowledge1);
        }
        eKnowledgeDao.addKnowledge(knowledge);
        return knowledge;
    }

    @Override
    public List<EKnowledge> getKnowledgeContainsChilds(String id) {
        List<EKnowledge> eKnowledges = eKnowledgeDao.getKnowledgeContainsChilds(id);
        return eKnowledges;
    }

    @Override
    public void modifKnowledge(EKnowledge knowledge) throws SysException {
        eKnowledgeDao.modifKnowledge(knowledge);
    }

    @Override
    public void modifKnowledgeTextOnly(String id, String knowledgeText) throws SysException {
        eKnowledgeDao.saveKnowledgeTextOnly(id, knowledgeText);
    }

    @Override
    public void delKnowledge(String id) throws SysException {
//        eKnowledgeDao.delKnowledge(id);
        List<EKnowledge> knowledges = this.getKnowledgeContainsChilds(id);
        for (EKnowledge knowledge : knowledges) {
            eKnowledgeDao.delKnowledge(knowledge.getId());
        }
    }

    @Override
    public EKnowledge createRootNode(String parentID, String gradeno, String subjectno) {
        EKnowledge result = new EKnowledge();
        EKnowledge knowledge = new EKnowledge();
        knowledge.setParentid(parentID);

        List<EKnowledge> knowledgeList = eKnowledgeDao.queryKnowledge(knowledge);
        if (knowledgeList.size() == 0) {
            EKnowledge eKnowledge = new EKnowledge();
            eKnowledge.setParentid(parentID);
            eKnowledge.setKnowledgeText(gradeno + subjectno);
            eKnowledge.setGradeno(gradeno);
            eKnowledge.setSubjectno(subjectno);
//            knowledge.setId(UUID.randomUUID().toString());
//            knowledge.setHaschild();
            try {
                result = this.addKnowledge(eKnowledge);
            } catch (Exception e) {
                e.printStackTrace();
                result = null;
            }
        } else {//存在指定根的节点
            for (EKnowledge eKnowledge : knowledgeList) {
                ReflectionUtil.copyProperties(eKnowledge, result);
            }
        }

        return result;
    }

    @Override
    public List<EKnowledge> getKnowledgeGrasping(String studentID, String rootID) throws SysException {
        List<EKnowledge> result = new ArrayList<>();
        //获取tree
        List<EKnowledge> knowledges = this.getKnowledgeContainsChilds(rootID);
        Float grasping = 0.0f;
        for (EKnowledge knowledge : knowledges) {
            //获取分子-指定学生、知识点的错题数
            EWrongKnowledgeDTO wrongKnowledgeDTO = new EWrongKnowledgeDTO();
            wrongKnowledgeDTO.setKnowledgeID(knowledge.getId());
            wrongKnowledgeDTO.setStudentID(studentID);
            int wrongNums = eKnowledgeDao.getWrongKnowledge(wrongKnowledgeDTO).size();
            //获取指定知识点、且学生参与过的题目数量
            int paperKnowledgeNums = eKnowledgeDao.getPaperKnowledge(knowledge.getId(), studentID).size();
            if (paperKnowledgeNums == 0) {
                knowledge.setGrasping(0.0f);
            } else {
                grasping = (float) wrongNums / paperKnowledgeNums;
                knowledge.setGrasping(grasping);
            }
            result.add(knowledge);
        }


        return result;
    }


}
