package com.extjs.controller;


import com.extjs.model.VPaperQuestionAndInfo;
import com.extjs.service.EpaperQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jenny on 2017/10/17.
 */
@Controller
@RequestMapping("paperQuestion")
public class EpaperQuestionController {
    @Autowired
    EpaperQuestionService epaperQuestionService;

    @RequestMapping("/getPaperQuestionList")
    @ResponseBody
    public Map getPaperQuestion(VPaperQuestionAndInfo vPaperQuestionAndInfo) {
        Map resultMap = new HashMap();
        String startDate = vPaperQuestionAndInfo.getStartDate().substring(0, 8);
        String endDate = vPaperQuestionAndInfo.getEndDate().substring(vPaperQuestionAndInfo.getEndDate().length() - 8, vPaperQuestionAndInfo.getEndDate().length());
        vPaperQuestionAndInfo.setStartDate(startDate);
        vPaperQuestionAndInfo.setEndDate(endDate);
        List<VPaperQuestionAndInfo> paperQuestionAndInfoList = epaperQuestionService.getPaperQuestionAndInfo(vPaperQuestionAndInfo);
        resultMap.put("data", paperQuestionAndInfoList);
        resultMap.put("total", paperQuestionAndInfoList.size());
        return resultMap;
    }

    @RequestMapping("/addPaperQuestion")
    @ResponseBody
    public Map addPaperQuestion(VPaperQuestionAndInfo vPaperQuestionAndInfo) {
        Map resultMap = new HashMap();
        try {
            epaperQuestionService.addPaperQuestionAndInfo(vPaperQuestionAndInfo);
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping("/mergePaperQuestion")
    @ResponseBody
    public Map mergePaperQuestion(VPaperQuestionAndInfo vPaperQuestionAndInfo) {
        Map resultMap = new HashMap();
        try {
            epaperQuestionService.mergePaperQuestionAndInfo(vPaperQuestionAndInfo);
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }
        return resultMap;
    }

    //    @RequestMapping("/getTestPaperList")
//    @ResponseBody
//    public Map getTestPaperList(VPaperQuestionAndInfo vPaperQuestionAndInfo) {
//        String startDate = vPaperQuestionAndInfo.getStartDate().substring(0, 8);
//        String endDate = vPaperQuestionAndInfo.getEndDate().substring(vPaperQuestionAndInfo.getEndDate().length() - 8, vPaperQuestionAndInfo.getEndDate().length());
//        vPaperQuestionAndInfo.setStartDate(startDate);
//        vPaperQuestionAndInfo.setEndDate(endDate);
//        Map resultMap = new HashMap();
//        List<ETestpaperDTO> testpaperDTOList = epaperQuestionService.getTestPaperListByTimeInterval(vPaperQuestionAndInfo);
//        resultMap.put("data", testpaperDTOList);
//        resultMap.put("total", testpaperDTOList.size());
//        return resultMap;
//    }
    @RequestMapping("/exportQuestions")
    @ResponseBody
    public void exportQuestions(HttpServletRequest request, VPaperQuestionAndInfo paperQuestionAndInfo, HttpServletResponse response) {
//        String startDate = paperQuestionAndInfo.getStartDate().substring(0, 8);
//        String endDate = paperQuestionAndInfo.getEndDate().substring(paperQuestionAndInfo.getEndDate().length() - 8, paperQuestionAndInfo.getEndDate().length());
//        paperQuestionAndInfo.setStartDate(startDate);
//        paperQuestionAndInfo.setEndDate(endDate);

        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();

        String path = epaperQuestionService.exportHTML(response, paperQuestionAndInfo, url);


    }


}
