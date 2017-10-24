package com.extjs.service.impl;

import com.extjs.dao.EpaperQuestionsDao;
import com.extjs.dao.EquestionDao;
import com.extjs.dao.EquestionInfoDao;
import com.extjs.model.*;
import com.extjs.service.EquestionService;
import com.extjs.service.EschoolService;
import com.extjs.service.UserService;
import com.extjs.util.ExportToHtml;
import com.extjs.util.ReflectionUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.sql.Date;

/**
 * Created by jenny on 2017/6/23.
 */
@Service
@Scope("prototype")
@Transactional
public class EquestionServiceImpl implements EquestionService {
    @Autowired
    EquestionDao equestionDao;
    @Autowired
    EquestionInfoDao equestionInfoDao;
    @Autowired
    EpaperQuestionsDao epaperQuestionsDao;
    @Autowired
    UserService userService;
    @Autowired
    EschoolService eschoolService;

    /**
     * 该方法已废弃，由getQuestionAndInfoList()代替（从视图中查询），啦啦啦
     *
     * @param eQuestionsDTO
     * @return
     */
    @Override
    public List<EQuestionsDTO> getQuestionList(EQuestionsDTO eQuestionsDTO) {
        EQuestions eQuestions = new EQuestions();
        ReflectionUtil.copyProperties(eQuestionsDTO, eQuestions);
        List<EQuestionsDTO> questionsDTOList = new ArrayList<>();
        List<EQuestions> questionsList = equestionDao.getQuestions(eQuestions);
//        Map resultMap = new HashMap();
        String id = "";
        String questionID = "";
        StringBuilder stringBuilder = new StringBuilder("");

        int i = 0;
        for (EQuestions questions : questionsList) {

            if (!questionID.equals(questions.getQuestionid())) {//不相等-新题干,把sb添加到结果集中形成一道新题干，设sb为空；此方法不能添加最后一条集合的记录
                if (stringBuilder.toString().length() > 0) {
                    eQuestionsDTO = new EQuestionsDTO();
                    eQuestionsDTO.setQuestion(
                            StringEscapeUtils.unescapeXml(stringBuilder.toString()).replaceFirst(".？&nbsp", ". &nbsp"));

                    eQuestionsDTO.setQuestionid(questionID);
//                    eQuestionsDTO.setQuestionid(questions.getQuestionid());
//                    eQuestionsDTO.setId(questions.getId());
                    eQuestionsDTO.setId(id);
                    questionsDTOList.add(eQuestionsDTO);
//                    resultMap.put(i, stringBuilder.toString().replaceFirst(".？&nbsp", ". &nbsp"));
                }
                stringBuilder = new StringBuilder("");
            }
            stringBuilder.append(questions.getQuestion());
            questionID = questions.getQuestionid();
            id = questions.getId();
            i++;
            if (i == questionsList.size()) {//最后一条记录加入到集合中
                eQuestionsDTO = new EQuestionsDTO();
                eQuestionsDTO.setQuestion(
                        StringEscapeUtils.unescapeXml(stringBuilder.toString()).replaceFirst(".？&nbsp", ". &nbsp"));
//                    eQuestionsDTO.setId(questions.getId());
                eQuestionsDTO.setQuestionid(questionID);
                eQuestionsDTO.setId(questions.getId());
                questionsDTOList.add(eQuestionsDTO);
            }
        }
        return questionsDTOList;
    }

    @Override
    public List<VQuestionandinfo> getQuestionAndInfoList(VQuestionandinfo questionandinfo) {

        List<VQuestionandinfo> questionandinfoList = equestionInfoDao.getQuestionAndInfos(questionandinfo);
        List<VQuestionandinfo> ResultQuestionandinfos = new ArrayList<>();
        VQuestionandinfo c_questionandinfo;
        String question = "";

        int i = 0;
        for (VQuestionandinfo vQuestionandinfo : questionandinfoList) {
            c_questionandinfo = new VQuestionandinfo();
            ReflectionUtil.copyProperties(vQuestionandinfo, c_questionandinfo);
            c_questionandinfo.setQuestion(
                    StringEscapeUtils.unescapeXml(vQuestionandinfo.getQuestion()).replaceFirst(".？&nbsp", ". &nbsp"));//反转义xml，使之满足html展示的格式要求


            if (vQuestionandinfo.getQuestionno() > 0) {


                ResultQuestionandinfos.remove(i);

//                vQuestionandinfo.setQuestion(question+vQuestionandinfo.getQuestion());//这种写法会引发hibernate持久化自动执行update

                c_questionandinfo.setQuestion(question +
                        StringEscapeUtils.unescapeXml(vQuestionandinfo.getQuestion()).replaceFirst(".？&nbsp", ". &nbsp"));


            }
            ResultQuestionandinfos.add(c_questionandinfo);
            question = ResultQuestionandinfos.get(i).getQuestion();
            i++;
        }
        return ResultQuestionandinfos;
    }

    @Override
    public String getOneQuestion(String questionID) {
        StringBuilder sb = null;
        if (null != questionID && !"".equals(questionID)) {
            EQuestions eQuestions = new EQuestions();
            eQuestions.setQuestionid(questionID);
            List<EQuestions> questionsList = equestionDao.getQuestions(eQuestions);
            for (EQuestions questions : questionsList) {
                sb.append(questions.getQuestion());
            }
        }

        return StringEscapeUtils.unescapeXml(sb.toString()).replaceFirst(".？&nbsp", ". &nbsp");
    }

    @Override
    public EQuestionsDTO getOneQuestionDTO(String uid) {
        EQuestionsDTO eQuestionsDTO = new EQuestionsDTO();
        EQuestions eQuestions = new EQuestions();
        eQuestions.setId(uid);
        List<EQuestions> eQuestionsList = equestionDao.getQuestions(eQuestions);
        for (EQuestions questions : eQuestionsList) {
            ReflectionUtil.copyProperties(questions, eQuestionsDTO);
        }
        return eQuestionsDTO;
    }

    @Override
    public void addOneQuestion(EQuestionsDTO eQuestionsDTO) {
        EQuestions eQuestions = new EQuestions();
        try {
            ReflectionUtil.copyProperties(eQuestionsDTO, eQuestions);
            equestionDao.addQuestion(eQuestions);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delOneQuestion(EQuestionsDTO eQuestionsDTO) {
        if (null != eQuestionsDTO.getQuestionid() && eQuestionsDTO.getQuestionid().length() > 0) {
            equestionDao.delQuestion(eQuestionsDTO.getQuestionid());
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void updateOneQuestionAndInfo(EQuestionsDTO eQuestionsDTO, EQuestionInfoDTO eQuestionInfoDTO) {
//        EQuestionsDTO questionsDTO=new EQuestionsDTO();
        equestionDao.delQuestion(eQuestionsDTO.getQuestionid());
        equestionInfoDao.delQuestionInfo(eQuestionsDTO.getQuestionid());
        this.addOneQuestionAndInfo(eQuestionsDTO.getQuestion(), eQuestionInfoDTO.getGradeno(), eQuestionInfoDTO.getSubjectno(), eQuestionInfoDTO.getQuestiontype(), eQuestionInfoDTO.getDifficulty());
    }

    @Override
    public void addOneQuestionInfo(EQuestionInfoDTO eQuestionInfoDTO) {
        EQuestionInfo eQuestionInfo = new EQuestionInfo();
        ReflectionUtil.copyProperties(eQuestionInfoDTO, eQuestionInfo);
        equestionInfoDao.saveOrUpdateQuestionInfo(eQuestionInfo);
//        return false;
    }

//    @Override
//    public void addOnePaperQuestion(EPaperQuestionsDTO ePaperQuestionsDTO) {
//        EPaperQuestions ePaperQuestions = new EPaperQuestions();
//        ReflectionUtil.copyProperties(ePaperQuestionsDTO, ePaperQuestions);
//        epaperQuestionsDao.addPaperQuestions(ePaperQuestions);
//    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void addOneQuestionAndInfo(String question, String gradeNo, String subjectNo, String questionType, Float difficulty) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String questionID = UUID.randomUUID().toString();
        int questionLength = 2000;
        String subQuestion;
        UserDTO userDTO = null;

        EQuestionsDTO eQuestionsDTO;
        EQuestionInfoDTO questionInfoDTO;
        for (int j = 0; j < question.length() / questionLength + 1; j++) {
            if (question.length() < (j + 1) * questionLength - 1) {
                subQuestion = question.substring(j * questionLength, question.length());
            } else {
                subQuestion = question.substring(j * questionLength, (j + 1) * questionLength - 1);
            }

            eQuestionsDTO = new EQuestionsDTO();
            eQuestionsDTO.setId(UUID.randomUUID().toString());
            eQuestionsDTO.setQuestionno(j);
            eQuestionsDTO.setQuestionid(questionID);
            eQuestionsDTO.setQuestion(StringEscapeUtils.escapeXml11(subQuestion));//转义题干xml
            try {
                this.addOneQuestion(eQuestionsDTO);//添加基础题库表
            } catch (Exception e) {
                System.out.print("添加基础题库表错误：--");
                e.printStackTrace();
            }
            questionInfoDTO = new EQuestionInfoDTO();
            questionInfoDTO.setQuestionid(eQuestionsDTO.getQuestionid());
            questionInfoDTO.setId(eQuestionsDTO.getId());
            questionInfoDTO.setGradeno(gradeNo);
            questionInfoDTO.setSubjectno(subjectNo);
            questionInfoDTO.setCreator(userDetails.getUsername());
            questionInfoDTO.setDifficulty(difficulty);
            try {
                userDTO = userService.getUserByUnique(userDetails.getUsername());
                questionInfoDTO.setSchoolno(eschoolService.querySchoolByUnique(userDTO.getuserSchool(), null).getSchoolno());
                questionInfoDTO.setQuestiontype(questionType);
                questionInfoDTO.setCreatedate(new Date(System.currentTimeMillis()));
                this.addOneQuestionInfo(questionInfoDTO);//添加试题信息表
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
//    public String exportToHTML(HttpServletResponse response , VQuestionandinfo questionandinfo) {
//        List<VQuestionandinfo> questionandinfoList = this.getQuestionAndInfoList(questionandinfo);
//        StringBuilder stringBuilder = new StringBuilder("");
//        int i = 0;
//        for (VQuestionandinfo vQuestionandinfo : questionandinfoList) {
//            i++;
//            stringBuilder.append("(" + i + ")." + vQuestionandinfo.getQuestion());
//        }
//        ExportToHtml exportToHtml = new ExportToHtml();
//        try {
//            return exportToHtml.exportToHtml(response, stringBuilder);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//
//    }
    public String exportToHTML(HttpServletResponse response, VQuestionandinfo questionandinfo, String url) {
        List<VQuestionandinfo> questionandinfoList = this.getQuestionAndInfoList(questionandinfo);
        StringBuilder stringBuilder = new StringBuilder("");
        int i = 0;
        for (VQuestionandinfo vQuestionandinfo : questionandinfoList) {
            i++;
            stringBuilder.append("(" + i + ")." + vQuestionandinfo.getQuestion());
        }
        ExportToHtml exportToHtml = new ExportToHtml();

        return exportToHtml.exportToHtml(response, stringBuilder, url);

    }
}
