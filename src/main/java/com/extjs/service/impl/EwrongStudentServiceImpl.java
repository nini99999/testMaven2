package com.extjs.service.impl;

import com.extjs.dao.EstudentDao;
import com.extjs.dao.EtestpaperDao;
import com.extjs.dao.EwrongStudentDao;
import com.extjs.model.*;
import com.extjs.service.*;
import com.extjs.util.ExportToHtml;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.*;

/**
 * Created by jenny on 2017/5/22.
 */
@Service
@Scope("prototype")
public class EwrongStudentServiceImpl implements EwrongStudentService {
    @Autowired
    private EwrongStudentDao ewrongStudentDao;
    @Autowired
    private EstudentService estudentService;
    @Autowired
    private EtestpaperService etestpaperService;
    @Autowired
    private EpaperQuestionService epaperQuestionService;
    @Autowired
    private EquestionService equestionService;


    @Override
    public EWrongStudentDTO getWrongStudentByID(String id) {
        EWrongStudentDTO wrongStudentDTO = new EWrongStudentDTO();
        wrongStudentDTO.setId(id);
        List<EWrongStudent> wrongStudentList = ewrongStudentDao.queryEWrongStudent(wrongStudentDTO);
        for (EWrongStudent wrongStudent : wrongStudentList) {
            ReflectionUtil.copyProperties(wrongStudent, wrongStudentDTO);
        }
        return wrongStudentDTO;
    }

    @Override
    public List<EWrongStudentDTO> queryWrongStudent(EWrongStudentDTO eWrongStudentDTO) {

        List<EWrongStudent> eWrongStudents = ewrongStudentDao.queryEWrongStudent(eWrongStudentDTO);

        List<EWrongStudentDTO> eWrongStudentDTOList = new ArrayList<EWrongStudentDTO>();
        for (EWrongStudent eWrongStudent : eWrongStudents) {
            eWrongStudentDTO = new EWrongStudentDTO();
            ReflectionUtil.copyProperties(eWrongStudent, eWrongStudentDTO);
            if (null != eWrongStudent.getTestpaperno() && eWrongStudent.getTestpaperno().length() > 0) {//试卷名赋值
                ETestpaperDTO testpaperDTO = etestpaperService.getTestPaperByTPNO(eWrongStudent.getTestpaperno());

                eWrongStudentDTO.setTestpapername(testpaperDTO.getTpname());
            }

            if (null != eWrongStudent.getStudentid() && eWrongStudent.getStudentid().length() > 0) {//学生姓名赋值

                String studentname = estudentService.getStudentByID(eWrongStudent.getStudentid()).getStudentname();
                eWrongStudentDTO.setStudentname(studentname);
            }
            eWrongStudentDTOList.add(eWrongStudentDTO);
        }
        return eWrongStudentDTOList;
    }

    @Override
    public HashMap<Integer, Integer> getQuestionno(String studentID) {
        HashMap<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
        if (null != studentID && studentID.length() > 0) {
            EWrongStudentDTO eWrongStudentDTO = new EWrongStudentDTO();
            eWrongStudentDTO.setStudentid(studentID);
            List<EWrongStudent> eWrongStudents = ewrongStudentDao.queryEWrongStudent(eWrongStudentDTO);
            for (EWrongStudent eWrongStudent : eWrongStudents) {
                resultMap.put(eWrongStudent.getQuestionno(), eWrongStudent.getQuestionno());
            }
        }
        return resultMap;
    }

    @Override
    public HashMap<String, String> getWrongByStudent(String studentID) {
        HashMap<String, String> resultMap = new HashMap<>();
        if (null != studentID && studentID.length() > 0) {
            EWrongStudentDTO eWrongStudentDTO = new EWrongStudentDTO();
            eWrongStudentDTO.setStudentid(studentID);
            List<EWrongStudent> eWrongStudents = ewrongStudentDao.queryEWrongStudent(eWrongStudentDTO);
            for (EWrongStudent eWrongStudent : eWrongStudents) {
                resultMap.put(eWrongStudent.getQuestionid(), eWrongStudent.getId());
            }

        }
        return resultMap;
    }

    @Override
    public List<EWrongStudentDTO> getQuestionListWithState(String paperid, String studentid, String currentUser) {
        if (null == studentid || studentid.length() == 0) {
            studentid = estudentService.getStudentByUserName(currentUser).getId();//根据当前登录的学生用户获取其id
        }
        EStudentDTO studentDTO = estudentService.getStudentByID(studentid);
        List<EWrongStudentDTO> eWrongStudentDTOList = new ArrayList<EWrongStudentDTO>();
        HashMap<String, String> studentWrongList = this.getWrongByStudent(studentid);//获取学生已登记错题
        ETestpaperDTO testpaperDTO = etestpaperService.getTestPaperByTPNO(paperid);
        VPaperQuestionAndInfo paperQuestionAndInfo = new VPaperQuestionAndInfo();
        /**查询试卷题目信息*/
        paperQuestionAndInfo.setPaperid(paperid);
        List<VPaperQuestionAndInfo> paperQuestionAndInfoList = epaperQuestionService.getPaperQuestionAndInfo(paperQuestionAndInfo);
        for (VPaperQuestionAndInfo vPaperQuestionAndInfo : paperQuestionAndInfoList) {
            EWrongStudentDTO wrongStudentDTO = new EWrongStudentDTO();
            wrongStudentDTO.setTestpaperno(paperid);
            wrongStudentDTO.setStudentid(studentid);
            wrongStudentDTO.setCountryid(studentDTO.getCountryid());
            wrongStudentDTO.setTestpapername(testpaperDTO.getTpname());
            wrongStudentDTO.setTestdate(testpaperDTO.getTestdate());
            wrongStudentDTO.setQuestionno(vPaperQuestionAndInfo.getPaperquestionno());
            wrongStudentDTO.setQuestionid(vPaperQuestionAndInfo.getQuestionid());
            if (null != studentWrongList.get(vPaperQuestionAndInfo.getQuestionid())) {
                wrongStudentDTO.setEstate(true);
            }
            eWrongStudentDTOList.add(wrongStudentDTO);
        }
        return eWrongStudentDTOList;
    }

    @Override
    public List<RWrongQuestion> getWrongsByDateArea(String beginDate, String endDate, String subjectno, String gradeno) {
        List<RWrongQuestion> wrongStudentDTOS = ewrongStudentDao.getWrongsByDateArea(beginDate, endDate, subjectno, gradeno);
        return wrongStudentDTOS;
    }

    @Override
    public void saveOrUpdateWrongStudent(EWrongStudentDTO eWrongStudentDTO) {

        Date date = new Date(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        eWrongStudentDTO.setCreator(userDetails.getUsername());
        eWrongStudentDTO.setCreatedate(date);
        if (null == eWrongStudentDTO.getId() || "".equals(eWrongStudentDTO.getId())) {
            UUID uuid = UUID.randomUUID();
            eWrongStudentDTO.setId(uuid.toString());
        }
        try {

            EWrongStudent eWrongStudent = new EWrongStudent();
            ReflectionUtil.copyProperties(eWrongStudentDTO, eWrongStudent);
            EStudentDTO eStudentDTO = estudentService.getStudentByID(eWrongStudentDTO.getStudentid());//获得学生姓名并赋值
            eWrongStudent.setCountryid(eStudentDTO.getCountryid());
            ewrongStudentDao.addEWrongStudent(eWrongStudent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delWrongStudent(EWrongStudentDTO eWrongStudentDTO) {
        try {
            EWrongStudent eWrongStudent = new EWrongStudent();
            ReflectionUtil.copyProperties(eWrongStudentDTO, eWrongStudent);
            ewrongStudentDao.delEWrongStudent(eWrongStudent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String modifdSelected(List<EWrongStudentDTO> eWrongStudentDTOList) {
        String res = "success";
        try {
            for (EWrongStudentDTO eWrongStudentDTO : eWrongStudentDTOList) {

                this.saveOrUpdateWrongStudent(eWrongStudentDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = e.toString();
        }

        return res;
    }

    @Override
    public List<VPaperQuestionAndInfo> getQuestionsByWrong(String userName, String paperid, String classno) {

        /*****查询指定试卷的所有题目,形成HashMap*****/
        HashMap<String, VPaperQuestionAndInfo> questionMap = new HashMap<>();
//        VPaperQuestionAndInfo paperQuestionAndInfo = new VPaperQuestionAndInfo();
//        paperQuestionAndInfo.setPaperid(paperid);
//        List<VPaperQuestionAndInfo> paperQuestionAndInfoList = epaperQuestionService.getPaperQuestionAndInfo(paperQuestionAndInfo);
//        for (VPaperQuestionAndInfo vPaperQuestionAndInfo : paperQuestionAndInfoList) {
//            questionMap.put(vPaperQuestionAndInfo.getQuestionid(), vPaperQuestionAndInfo);
//        }
        /*****查询学生(或班级的)指定试卷错题记录（含questionid）*****/

        EWrongStudentDTO wrongStudentDTO = new EWrongStudentDTO();

        wrongStudentDTO.setTestpaperno(paperid);
        if (null != classno && classno.length() > 0) {//说明按班级查询,则把studentid置为空
            wrongStudentDTO.setClassno(classno);
            wrongStudentDTO.setStudentid(null);
        } else {//说明当前登录用户为学生，则根据当前登录用户名获取其studentid
            wrongStudentDTO.setStudentid(estudentService.getStudentByUserName(userName).getId());
        }

        List<EWrongStudentDTO> wrongStudentDTOList = this.queryWrongStudent(wrongStudentDTO);
        VPaperQuestionAndInfo vPaperQuestionAndInfo = new VPaperQuestionAndInfo();
        VPaperQuestionAndInfo paperQuestionAndInfo = new VPaperQuestionAndInfo();
        List<VPaperQuestionAndInfo> resultList = new ArrayList<>();
        List<VPaperQuestionAndInfo> paperQuestionAndInfoList;
        for (EWrongStudentDTO eWrongStudentDTO : wrongStudentDTOList) {//循环获取题目集合
//            vPaperQuestionAndInfo = questionMap.get(eWrongStudentDTO.getQuestionid());
            paperQuestionAndInfo = new VPaperQuestionAndInfo();
            vPaperQuestionAndInfo = new VPaperQuestionAndInfo();
            paperQuestionAndInfo.setPaperid(paperid);
            paperQuestionAndInfo.setQuestionid(eWrongStudentDTO.getQuestionid());

            paperQuestionAndInfoList = epaperQuestionService.getPaperQuestionAndInfo(paperQuestionAndInfo);
            for (VPaperQuestionAndInfo paperQuestionAndInfo1 : paperQuestionAndInfoList) {

                ReflectionUtil.copyProperties(paperQuestionAndInfo1, vPaperQuestionAndInfo);
            }
            vPaperQuestionAndInfo.setId(eWrongStudentDTO.getStudentid());//把学生id赋值到主键中，供后续方法调用
            resultList.add(vPaperQuestionAndInfo);
        }
        return resultList;
    }

    @Override
    public String exportHTML(HttpServletResponse response, String userName, String classno, String paperid, String url) throws SysException {

        List<VPaperQuestionAndInfo> paperQuestionAndInfoList = this.getQuestionsByWrong(userName, paperid, classno);
        StringBuilder stringBuilder = new StringBuilder("");
        int i = 0;
        String studentName="";
        String cid="";
        for (VPaperQuestionAndInfo paperQuestionAndInfo : paperQuestionAndInfoList) {

            if (cid.equals(paperQuestionAndInfo.getId())){
                i++;
            }else {
                i=1;
                cid=paperQuestionAndInfo.getId();
            }

            studentName=estudentService.getStudentByID(paperQuestionAndInfo.getId()).getStudentname();
            stringBuilder.append("<p><strong>--------学生：").append(studentName).append(",错题：(" + i + ").--------</strong></p>" + paperQuestionAndInfo.getQuestion());
            //查询错题原因、分析和解决策略
//            EStudentDTO studentDTO = estudentService.getStudentByUserName(userName);//查询学生id
//            EWrongStudent wrongStudent = ewrongStudentDao.getByUnique(paperQuestionAndInfo.getQuestionid(), studentDTO.getId());
            EWrongStudent wrongStudent = ewrongStudentDao.getByUnique(paperQuestionAndInfo.getQuestionid(), paperQuestionAndInfo.getId());
            //paperQuestionAndInfo中的id存放的是学生id
            if (null != wrongStudent.getReason() && wrongStudent.getReason().length() > 0) {
                stringBuilder.append("<p>【错题原因】</p><p>").append(wrongStudent.getReason()).append("</p>");
            }
            if (null != wrongStudent.getAnalysis() && wrongStudent.getAnalysis().length() > 0) {
                stringBuilder.append("<p>【错因分析】</p><p>").append(wrongStudent.getAnalysis()).append("</p>");
            }
            if (null!=wrongStudent.getSolution()&&wrongStudent.getSolution().length()>0){
                stringBuilder.append("<p>【解决策略】</p><p>").append(wrongStudent.getSolution()).append("</p>");
            }
//            stringBuilder.append("<p>【错题原因】</p><p>").append(wrongStudent.getReason()).append("</p><p>【错因分析】</p><p>").append(wrongStudent.getAnalysis())
//                    .append("</p><p>【解决策略】</p><p>").append(wrongStudent.getSolution()).append("</p>");
        }
        ExportToHtml exportToHtml = new ExportToHtml();

        return exportToHtml.exportToHtml(response, stringBuilder, url);
    }

    @Override
    public void saveWrong(EWrongStudentDTO eWrongStudentDTO) throws SysException {
        try {
            EWrongStudent eWrongStudent = new EWrongStudent();
            ReflectionUtil.copyProperties(eWrongStudentDTO, eWrongStudent);
            ewrongStudentDao.saveWrongAnalysis(eWrongStudent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
