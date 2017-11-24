package com.extjs.service.impl;

import com.extjs.dao.EpaperQuestionsDao;
import com.extjs.dao.EquestionInfoDao;
import com.extjs.model.*;
import com.extjs.service.*;
import com.extjs.util.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
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
    private EpaperQuestionsDao epaperQuestionsDao;
    @Autowired
    private UserService userService;
    @Autowired
    private EschoolService eschoolService;
    @Autowired
    private EquestionService equestionService;
    @Autowired
    private EquestionInfoDao equestionInfoDao;
    @Autowired
    private EtestpaperService etestpaperService;
    @Autowired
    private EAnswerService eAnswerService;

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
    public void addPaperQuestionAndInfoList(List<List<String>> questionTypeList, List<EPaperQTypeDTO> paperQTypeDTOList, String paperid, String gradeno, String subjectno) throws SysException {
        VPaperQuestionAndInfo paperQuestionAndInfo;
        int j = 0;
        int paperQuestionNo = 0;
        for (List<String> list : questionTypeList) {
            j++;

            String questionType = paperQTypeDTOList.get(j).getQuestiontype();

            for (String string : list) {
                paperQuestionNo++;
                paperQuestionAndInfo = new VPaperQuestionAndInfo();
                paperQuestionAndInfo.setQuestion(string);
                paperQuestionAndInfo.setQuestiontype(questionType);
                paperQuestionAndInfo.setPaperid(paperid);
                paperQuestionAndInfo.setGradeno(gradeno);
                paperQuestionAndInfo.setSubjectno(subjectno);
                paperQuestionAndInfo.setPaperquestionno(paperQuestionNo);//设置试卷中的题号
                this.addPaperQuestionAndInfo(paperQuestionAndInfo);
            }

//                        equestionService.addOneQuestionAndInfo(string, gradeno, subjectno, questionType, Float.parseFloat("0"));
//                        dom4JforXML.getPaperQuestionsBySpecify(string, map.size());
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void addPaperQuestionAndInfo(VPaperQuestionAndInfo vPaperQuestionAndInfo) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String questionID = UUID.randomUUID().toString();
        int questionLength = EConstants.questionLength;
        String subQuestion;
        UserDTO userDTO;

        EPaperQuestionsDTO ePaperQuestionsDTO;
        String answerContent = "";
        String question = vPaperQuestionAndInfo.getQuestion();
        /*****如果题目信息中包括【答案】，则把答案部分删除，仅保留题干信息****/
        if (vPaperQuestionAndInfo.getQuestion().indexOf(EConstants.answerIdentifier) > -1) {
            answerContent = vPaperQuestionAndInfo.getQuestion().substring(
                    vPaperQuestionAndInfo.getQuestion().indexOf(EConstants.answerIdentifier) + EConstants.answerIdentifier.length());
            question = question.replace(EConstants.answerIdentifier + answerContent, "");
        }


        for (int j = 0; j < question.length() / questionLength + 1; j++) {
            if (question.length() < (j + 1) * questionLength - 1) {
                subQuestion = question.substring(j * questionLength, question.length());
            } else {
                subQuestion = question.substring(j * questionLength, (j + 1) * questionLength);
            }

            ePaperQuestionsDTO = new EPaperQuestionsDTO();
            ReflectionUtil.copyProperties(vPaperQuestionAndInfo, ePaperQuestionsDTO);
            ePaperQuestionsDTO.setId(UUID.randomUUID().toString());
            ePaperQuestionsDTO.setQuestionno(j);

            ePaperQuestionsDTO.setQuestionid(questionID);
            ePaperQuestionsDTO.setQuestion(StringEscapeUtils.escapeXml11(subQuestion));//转义题干xml
            EQuestionsDTO questionsDTO = new EQuestionsDTO();
            questionsDTO.setId(UUID.randomUUID().toString());
            questionsDTO.setQuestion(ePaperQuestionsDTO.getQuestion());
            questionsDTO.setQuestionid(questionID);
            questionsDTO.setQuestionno(j);
            questionsDTO.setCreator(userDetails.getUsername());
            try {
                equestionService.addOneQuestion(questionsDTO);//添加至试题基础库
                this.addOnePaperQuestion(ePaperQuestionsDTO);//添加至试卷试题表

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        /**添加试题信息表**/
        EQuestionInfoDTO questionInfoDTO = new EQuestionInfoDTO();
        ReflectionUtil.copyProperties(vPaperQuestionAndInfo, questionInfoDTO);
        questionInfoDTO.setQuestionid(questionID);
        questionInfoDTO.setId(UUID.randomUUID().toString());
        questionInfoDTO.setCreator(userDetails.getUsername());
        try {
            userDTO = userService.getUserByUnique(userDetails.getUsername());
            questionInfoDTO.setSchoolno(eschoolService.querySchoolByUnique(userDTO.getuserSchool(), null).getSchoolno());
            questionInfoDTO.setCreatedate(new Date(System.currentTimeMillis()));

            equestionService.addOneQuestionInfo(questionInfoDTO);

            /**添加试题答案表*/
            if (answerContent.length() > 0) {
                EAnswer answer = new EAnswer();
                answer.setId(questionID);
                RegexString regexString = new RegexString();

                answer.setAnswer(regexString.delHTMLTag(answerContent));//删除html元素
                eAnswerService.saveAnswer(answer);
            }

        } catch (Exception e) {
            e.printStackTrace();
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
//同步删除试题基础库表、试题信息表
            this.delPaperQuestion(paperQuestionsDTO);
            EQuestionsDTO questionsDTO = new EQuestionsDTO();
            questionsDTO.setQuestionid(vPaperQuestionAndInfo.getQuestionid());
            equestionService.delOneQuestion(questionsDTO);
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
                resultList.remove(i - 1);
                paperQuestionAndInfo.setQuestion(question + vPaperQuestionAndInfo1.getQuestion().replaceFirst(".？&nbsp", ". &nbsp"));
                resultList.add(paperQuestionAndInfo);
                question = resultList.get(i - 1).getQuestion();
            } else {
                resultList.add(paperQuestionAndInfo);
                question = resultList.get(i).getQuestion();
                i++;
            }
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

    @Override
    public String exportHTML(HttpServletResponse response, VPaperQuestionAndInfo paperQuestionAndInfo, String url) {
        List<VPaperQuestionAndInfo> paperQuestionAndInfoList = this.getPaperQuestionAndInfo(paperQuestionAndInfo);
        StringBuilder stringBuilder = new StringBuilder("");
        int i = 0;
        for (VPaperQuestionAndInfo vPaperQuestionAndInfo : paperQuestionAndInfoList) {
            i++;
            stringBuilder.append("(" + i + ")." + vPaperQuestionAndInfo.getQuestion());
        }
        ExportToHtml exportToHtml = new ExportToHtml();

        return exportToHtml.exportToHtml(response, stringBuilder, url);
    }
}
