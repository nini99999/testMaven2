package com.extjs.service.impl;

import com.extjs.dao.EpaperQuestionsDao;
import com.extjs.dao.EquestionInfoDao;
import com.extjs.model.*;
import com.extjs.service.*;
import com.extjs.util.ReflectionUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by jenny on 2017/10/14.
 */

@Service
@Scope("prototype")
@Transactional
public class EpaperQuestionServiceImpl implements EpaperQuestionService {
    @Autowired
    EpaperQuestionsDao epaperQuestionsDao;
    @Autowired
    UserService userService;
    @Autowired
    EschoolService eschoolService;
    @Autowired
    EquestionService equestionService;
    @Autowired
    EquestionInfoDao equestionInfoDao;
    @Autowired
    EtestpaperService etestpaperService;

    @Override
    public void addOnePaperQuestion(EPaperQuestionsDTO ePaperQuestionsDTO) {
        EPaperQuestions ePaperQuestions = new EPaperQuestions();
        ReflectionUtil.copyProperties(ePaperQuestionsDTO, ePaperQuestions);

        epaperQuestionsDao.addPaperQuestions(ePaperQuestions);
    }

    @Override
    public void delPaperQuestion(EPaperQuestionsDTO ePaperQuestionsDTO) {
        EPaperQuestions ePaperQuestions = new EPaperQuestions();
        ReflectionUtil.copyProperties(ePaperQuestionsDTO, ePaperQuestions);
        epaperQuestionsDao.delPaperQuestions(ePaperQuestions);
    }

    @Override
    public List<EPaperQuestionsDTO> queryPaperQuestions(EPaperQuestionsDTO ePaperQuestionsDTO) {
        EPaperQuestions ePaperQuestions = new EPaperQuestions();
        ReflectionUtil.copyProperties(ePaperQuestionsDTO, ePaperQuestions);
        List<EPaperQuestions> paperQuestionsList = epaperQuestionsDao.getPaperQuestions(ePaperQuestions);
        List<EPaperQuestionsDTO> resultMap = new ArrayList<>();
        for (EPaperQuestions paperQuestions : paperQuestionsList) {
            ReflectionUtil.copyProperties(paperQuestions, ePaperQuestionsDTO);
            resultMap.add(ePaperQuestionsDTO);
        }
        return resultMap;
    }

    @Override
    public void mergePaperQuestion(EPaperQuestionsDTO ePaperQuestionsDTO) {
        EPaperQuestions paperQuestions = new EPaperQuestions();
        ReflectionUtil.copyProperties(ePaperQuestionsDTO, paperQuestions);
        epaperQuestionsDao.mergePaperQuestion(paperQuestions);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void addPaperQuestionAndInfo(VPaperQuestionAndInfo vPaperQuestionAndInfo) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String questionID = UUID.randomUUID().toString();
        int questionLength = 2000;
        String subQuestion;
        UserDTO userDTO = null;
        EQuestionInfoDTO equestionInfoDTO;
        EPaperQuestionsDTO ePaperQuestionsDTO;
        String question = vPaperQuestionAndInfo.getQuestion();
        for (int j = 0; j < question.length() / questionLength + 1; j++) {
            if (question.length() < (j + 1) * questionLength - 1) {
                subQuestion = question.substring(j * questionLength, question.length());
            } else {
                subQuestion = question.substring(j * questionLength, (j + 1) * questionLength - 1);
            }

            ePaperQuestionsDTO = new EPaperQuestionsDTO();
            ReflectionUtil.copyProperties(vPaperQuestionAndInfo, ePaperQuestionsDTO);
            ePaperQuestionsDTO.setId(UUID.randomUUID().toString());
            ePaperQuestionsDTO.setQuestionno(j);

            ePaperQuestionsDTO.setQuestionid(questionID);

//            ePaperQuestionsDTO.setPaperid(vPaperQuestionAndInfo.getPaperid());
//            ePaperQuestionsDTO.setPaperquestionno(vPaperQuestionAndInfo.getPaperquestionno());
//            ePaperQuestionsDTO.setQuestionpoints(vPaperQuestionAndInfo.getQuestionpoints());
            ePaperQuestionsDTO.setQuestion(StringEscapeUtils.escapeXml11(subQuestion));//转义题干xml

            try {

                this.addOnePaperQuestion(ePaperQuestionsDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
            EQuestionInfoDTO questionInfoDTO = new EQuestionInfoDTO();
            ReflectionUtil.copyProperties(vPaperQuestionAndInfo, questionInfoDTO);
            questionInfoDTO.setQuestionid(questionID);
            questionInfoDTO.setId(UUID.randomUUID().toString());
////            questionInfoDTO.setGradeno(gradeNo);
////            questionInfoDTO.setSubjectno(subjectNo);
            questionInfoDTO.setCreator(userDetails.getUsername());
////            questionInfoDTO.setDifficulty(difficulty);

            try {
                userDTO = userService.getUserByUnique(userDetails.getUsername());
                questionInfoDTO.setSchoolno(eschoolService.querySchoolByUnique(userDTO.getuserSchool(), null).getSchoolno());
//                questionInfoDTO.setQuestiontype(questionType);
                questionInfoDTO.setCreatedate(new Date(System.currentTimeMillis()));

                equestionService.addOneQuestionInfo(questionInfoDTO);//添加试题信息表
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void mergePaperQuestionAndInfo(VPaperQuestionAndInfo vPaperQuestionAndInfo) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            UserDTO userDTO = userService.getUserByUnique(userDetails.getUsername());
            vPaperQuestionAndInfo.setCreator(userDetails.getUsername());
            vPaperQuestionAndInfo.setSchoolno(eschoolService.querySchoolByUnique(userDTO.getuserSchool(), null).getSchoolno());
            EPaperQuestionsDTO paperQuestionsDTO = new EPaperQuestionsDTO();
            EQuestionInfoDTO questionInfoDTO = new EQuestionInfoDTO();

//        paperQuestionsDTO.setId(vPaperQuestionAndInfo.getId());
            ReflectionUtil.copyProperties(vPaperQuestionAndInfo, paperQuestionsDTO);
            ReflectionUtil.copyProperties(vPaperQuestionAndInfo, questionInfoDTO);

            this.delPaperQuestion(paperQuestionsDTO);
            equestionInfoDao.delQuestionInfo(questionInfoDTO.getQuestionid());
            this.addPaperQuestionAndInfo(vPaperQuestionAndInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<VPaperQuestionAndInfo> getPaperQuestionAndInfo(VPaperQuestionAndInfo vPaperQuestionAndInfo) {
        List<VPaperQuestionAndInfo> paperQuestionAndInfoList = epaperQuestionsDao.getPaperQuestionAndInfo(vPaperQuestionAndInfo);
        List<VPaperQuestionAndInfo> resultList = new ArrayList<>();
        VPaperQuestionAndInfo paperQuestionAndInfo;
        String question = "";

        int i = 0;
        for (VPaperQuestionAndInfo vPaperQuestionAndInfo1 : paperQuestionAndInfoList) {
            paperQuestionAndInfo = new VPaperQuestionAndInfo();
            ReflectionUtil.copyProperties(vPaperQuestionAndInfo1, paperQuestionAndInfo);
            paperQuestionAndInfo.setQuestion(
                    StringEscapeUtils.unescapeXml(vPaperQuestionAndInfo1.getQuestion().replaceFirst(".？&nbsp", ". &nbsp")));
            if (vPaperQuestionAndInfo1.getQuestionno() > 0) {
                resultList.remove(i);
                paperQuestionAndInfo.setQuestion(question + vPaperQuestionAndInfo1.getQuestion().replaceFirst(".？&nbsp", ". &nbsp"));
            }
            resultList.add(paperQuestionAndInfo);
            question = resultList.get(i).getQuestion();
            i++;
        }

        return resultList;
    }

    @Override
    public List<ETestpaperDTO> getTestPaperListByTimeInterval(VPaperQuestionAndInfo vPaperQuestionAndInfo) {
        List<VPaperQuestionAndInfo> paperQuestionAndInfoList = this.getPaperQuestionAndInfo(vPaperQuestionAndInfo);
        HashMap<String, String> testPaperMap = etestpaperService.getEtestPaper();
        ETestpaperDTO testpaperDTO = null;
        List<ETestpaperDTO> resultMap = new ArrayList<>();
        for (VPaperQuestionAndInfo paperQuestionAndInfo : paperQuestionAndInfoList) {
            testpaperDTO = new ETestpaperDTO();
            testpaperDTO.setTpno(paperQuestionAndInfo.getPaperid());
            testpaperDTO.setTpname(testPaperMap.get(paperQuestionAndInfo.getPaperid()));
            resultMap.add(testpaperDTO);
        }
        return resultMap;
    }
}
