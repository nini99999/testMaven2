package com.extjs.controller;

import com.extjs.model.*;
import com.extjs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jenny on 2017/5/22.
 */
@Controller
@RequestMapping("ewrongStudent")
public class EwrongStudentController {
    @Autowired
    private EwrongStudentService ewrongStudentService;
    @Autowired
    private EstudentService estudentService;

    @RequestMapping("/viewWrongStudent")
    @ResponseBody
    public Map<String, Object> queryWrongStudent(String tpno, String student, String classno) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {

            EWrongStudentDTO wrongStudentDTO = new EWrongStudentDTO();
//            wrongStudentDTO.setCountryid(countryid.trim());

            if ("forStudent".equals(student)) {
                wrongStudentDTO.setStudentid(estudentService.getStudentByUserName(this.getCurrentUser()).getId());
            } else {
                if (null != classno && classno.length() > 0) {
                    wrongStudentDTO.setClassno(classno);
                }
            }
            wrongStudentDTO.setTestpaperno(tpno.trim());
            List<EWrongStudentDTO> wrongStudentDTOS = ewrongStudentService.queryWrongStudent(wrongStudentDTO);
            resultMap.put("data", wrongStudentDTOS);
            resultMap.put("total", wrongStudentDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @RequestMapping("/getCurrentUser")
    @ResponseBody
    public String getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }


    @RequestMapping("/getQuestionListWithState")
    @ResponseBody
    public Map<String, Object> getQuestionListWithState(String paperid, String studentid) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<EWrongStudentDTO> eWrongStudentDTOList =
                ewrongStudentService.getQuestionListWithState(paperid, studentid, this.getCurrentUser());
        resultMap.put("data", eWrongStudentDTOList);
        resultMap.put("total", eWrongStudentDTOList.size());

        return resultMap;
    }

    /**
     * 查询出试卷小题总数（要求数据库中试卷id存储为顺序整形），拼装为"学生错题记录"集合，针对已登记错题，设置estate为true（页面显示为选中）
     * 待解决：将学生错题记录表中的questionno设置为int
     *
     * @param tpno
     * @param studentid
     * @param testdate
     * @return
     * @RequestMapping("/getQuestionNumList")
     * @ResponseBody public Map<String, Object> getQuestionNumList(String tpno, String studentid) {
     * Map<String, Object> resultMap = new HashMap<String, Object>();
     * Integer num = etestpaperService.getSumQuestionNum(tpno);
     * if (null == studentid || studentid.length() == 0) {
     * studentid = estudentService.getStudentByUserName(this.getCurrentUser()).getId();//根据当前登录的学生用户获取其id
     * }
     * EStudentDTO studentDTO = estudentService.getStudentByID(studentid);
     * List<EWrongStudentDTO> eWrongStudentDTOList = new ArrayList<EWrongStudentDTO>();
     * <p>
     * HashMap<Integer, Integer> questionnoMap = ewrongStudentService.getQuestionno(studentid);//获取指定学生已有的错题
     * //        ETestpaperDTO testpaperDTO=new ETestpaperDTO();
     * //        testpaperDTO.setTpno(tpno);
     * ETestpaperDTO testpaperDTO = etestpaperService.getTestPaperByTPNO(tpno);
     * <p>
     * <p>
     * try {
     * for (int i = 0; i < num; i++) {
     * EWrongStudentDTO wrongStudentDTO = new EWrongStudentDTO();
     * wrongStudentDTO.setTestpaperno(tpno);
     * wrongStudentDTO.setStudentid(studentid);
     * wrongStudentDTO.setCountryid(studentDTO.getCountryid());
     * wrongStudentDTO.setTestpapername(testpaperDTO.getTpname());
     * wrongStudentDTO.setTestdate(testpaperDTO.getTestdate());
     * wrongStudentDTO.setQuestionno(i + 1);
     * <p>
     * if (null != questionnoMap.get(i + 1)) {
     * wrongStudentDTO.setEstate(true);
     * }
     * eWrongStudentDTOList.add(wrongStudentDTO);
     * }
     * resultMap.put("data", eWrongStudentDTOList);
     * resultMap.put("total", eWrongStudentDTOList.size());
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * <p>
     * return resultMap;
     * }
     */
    @RequestMapping("/modifWrongStudent")
    @ResponseBody
    public Map<String, Object> modifWrongStudent(@RequestBody List<EWrongStudentDTO> wrongStudentDTOS) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String str = ewrongStudentService.modifdSelected(wrongStudentDTOS);
            if ("success".equals(str)) {
                resultMap.put("success", true);
                resultMap.put("msg", "保存成功!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "保存失败!" + e.getMessage());
        }

        return resultMap;
    }

    @RequestMapping("/delSelects")
    @ResponseBody
    public Map<String, Object> delSelects(@RequestBody List<EWrongStudentDTO> wrongStudentDTOS) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            for (EWrongStudentDTO wrongStudentDTO : wrongStudentDTOS) {
                ewrongStudentService.delWrongStudent(wrongStudentDTO);
                resultMap.put("success", true);
                resultMap.put("msg", "删除成功!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "删除失败!" + e.getMessage());
        }

        return resultMap;
    }

    @RequestMapping("/delWrongStudent")
    @ResponseBody
    public Map<String, Object> delWrongStudent(String studentid, String countryid, String tpno) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            EWrongStudentDTO eWrongStudentDTO = new EWrongStudentDTO();
            eWrongStudentDTO.setStudentid(studentid);
            eWrongStudentDTO.setCountryid(countryid);
            eWrongStudentDTO.setTestpaperno(tpno);
            ewrongStudentService.delWrongStudent(eWrongStudentDTO);
            resultMap.put("success", true);
            resultMap.put("msg", "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "删除失败!" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping("/exportWrongQuestions")
    @ResponseBody
    public void exportWrongQuestions(HttpServletRequest request, HttpServletResponse response, String studentid, String subjectno, String gradeno, String classno, String paperid) {
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
        try {
            String path = ewrongStudentService.exportHTML(response, studentid, subjectno, gradeno, classno, paperid, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
