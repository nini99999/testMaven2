package com.extjs.controller;

import com.extjs.model.*;
import com.extjs.service.ReportService;
import com.extjs.util.EConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jenny on 2017/4/9.
 */
@Controller
@RequestMapping("report")

public class ReportController {
    @Autowired
    private ReportService reportService;

    private String splitTpnoString(String tpnoString) {
        String res = "";
        if (tpnoString.replace("\"", "").length() > 0) {

            String[] array = tpnoString.substring(1, tpnoString.length() - 1).split(",");
            if (array.length > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < array.length; i++) {
                    stringBuilder.append("'" + array[i].substring(1, array[i].length() - 1) + "',");
                }

                res = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
            }
        }
        return res;
    }

    public HashMap<String, String> getDateArea(String beginDate, String endDate) {
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("begin", beginDate.substring(0, 8));

        resultMap.put("end", endDate.substring(endDate.length() - 8, endDate.length()));
        return resultMap;
    }

    @RequestMapping("/queryRClassMark")
    @ResponseBody
    public Map queryRClassMark(RClassMark rClassMark) {
        Map<String, String> dateArea = this.getDateArea(rClassMark.getBeginDate(), rClassMark.getEndDate());
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            rClassMark.setBeginDate(dateArea.get("begin"));
            rClassMark.setEndDate(dateArea.get("end"));
            List resultList = reportService.reckonClassMark(rClassMark);
            resultMap.put("data", resultList);
            resultMap.put("total", resultList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @RequestMapping(value = "/queryRMarkArea", method = RequestMethod.POST)
    @ResponseBody
    public Map queryRMarkArea(String gradeno, String subjectno, String tpno, String tpnoString) {
        if (this.splitTpnoString(tpnoString).length() > 0) {
            tpno = this.splitTpnoString(tpnoString);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        RMarkArea rMarkArea = new RMarkArea();
        List resultList = reportService.queryRMarkArea(gradeno, tpno, subjectno);
//        List<RMarkArea> rMarkAreaList = reportService.queryRMarkArea(rMarkArea);
        resultMap.put("data", resultList);
        resultMap.put("total", resultList.size());
        return resultMap;
    }

    @RequestMapping("/getMarkArea")
    @ResponseBody
    /**
     * 获取预设的成绩区间
     */
    public String[] getMarkArea(String subjectType) {
        String[] cons;
        if (null != subjectType && !"".equals(subjectType)) {
            cons = EConstants.subjectMarkArea;
        } else {
            cons = EConstants.markArea;
        }

//        ArrayUtils.add(cons, 0, "班级");
        return cons;
    }

    @RequestMapping("/queryWrongQuestion")
    @ResponseBody
    public Map queryWrongQuestion(String beginDate, String endDate, String subjectno, String gradeno, String classno) {
        Map<String, String> dateArea = this.getDateArea(beginDate, endDate);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<RWrongQuestion> rWrongQuestions = reportService.queryRWrongQuestion(dateArea.get("begin"), dateArea.get("end"), subjectno, gradeno, classno);
        resultMap.put("data", rWrongQuestions);
        resultMap.put("total", rWrongQuestions.size());
        return resultMap;
    }

    /**
     * 获取升学成绩模拟数据
     *
     * @param gradeno
     * @param dateArea
     * @param aboveMark
     * @return
     */
    @RequestMapping("/queryAboveMark")
    @ResponseBody
    public Map queryAboveMark(String gradeno, String aboveMark, String tpnoString) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
//        Map<String, String> dateArea = this.getDateArea(beginDate,endDate);

        List<RAboveSpecifiedMark> aboveSpecifiedMarks =
                reportService.queryRAboveSpecifiedMark(gradeno, aboveMark, this.splitTpnoString(tpnoString));
        resultMap.put("data", aboveSpecifiedMarks);
        resultMap.put("total", aboveSpecifiedMarks.size());
        return resultMap;
    }

    /**
     * 获取年度教学成绩
     *
     * @param year
     * @param gradeno
     * @param subjectno
     * @return
     */
    @RequestMapping("/queryYearMark")
    @ResponseBody
    public Map queryYearMark(String year, String gradeno, String subjectno, String studentno, String studentname) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<String[]> rYearMarks = reportService.queryRYearMark(year, gradeno, subjectno, studentno, studentname);
        resultMap.put("data", rYearMarks);
        resultMap.put("total", rYearMarks.size());
        return resultMap;
    }

    /**
     * 获取学生年度考试成绩
     *
     * @param year
     * @param studentname
     * @param subjectno
     * @return
     */
    @RequestMapping("/queryYearMarkStudent")
    @ResponseBody
    public Map queryYearMarkStudent(String year, String studentID) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<String[]> rYearMarkStudents = reportService.getYearMarkStudent(year, studentID);
        resultMap.put("data", rYearMarkStudents);
        resultMap.put("total", rYearMarkStudents.size());
        return resultMap;
    }


    @RequestMapping("/getAvgMarkByClass")
    @ResponseBody
    public Map getAvgMarkByClass(String studentID, String year) {
        Map<String, Object> resultMap = new HashMap<>();
        Float[] floats = reportService.getAvgMarkByClass(studentID, year);
        resultMap.put("data", floats);
        return resultMap;
    }

    @RequestMapping("/getAvgMarkByGrade")
    @ResponseBody
    public Map getAvgMarkByGrade(String studentID, String year) {
        Map<String, Object> resultMap = new HashMap<>();
        Float[] floats = reportService.getAvgMarkByGrade(studentID, year);
        resultMap.put("data", floats);
        return resultMap;
    }
}
