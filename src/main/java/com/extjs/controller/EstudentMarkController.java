package com.extjs.controller;

import com.extjs.model.EStudentMark;
import com.extjs.model.Page;
import com.extjs.service.EstudentMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jenny on 2017/5/12.
 */
@Controller
@RequestMapping("studentMark")
public class EstudentMarkController {
    @Autowired
    private EstudentMarkService estudentMarkService;

    @RequestMapping("/queryEstudentMark")
    @ResponseBody
    public Map queryEstudentMark(EStudentMark eStudentMark, Page page) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int totalCount = estudentMarkService.getTotalCount(eStudentMark);
//        page.setTotalCount(totalCount);
        List<EStudentMark> eStudentMarkList = estudentMarkService.queryEStudentMark(eStudentMark, page);
//        resultMap.put("data", eStudentMarkList);
        resultMap.put("total", totalCount);
        resultMap.put("rows", eStudentMarkList);
        return resultMap;
    }

    @RequestMapping(value = "/saveOrUpdateEstudentMark", method = RequestMethod.POST)
    @ResponseBody
    public Map saveOrUpdateEstudentMark(EStudentMark eStudentMark) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            estudentMarkService.modifEStudentMark(eStudentMark);
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/modifOnlyMark", method = RequestMethod.POST)
    @ResponseBody
    public Map modifOnlyMark(EStudentMark eStudentMark) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            estudentMarkService.modifOnlyMark(eStudentMark);
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/delEstudentMark", method = RequestMethod.POST)
    @ResponseBody
    public Map delEstudentMark(@RequestBody List<EStudentMark> eStudentMarks) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            for (EStudentMark eStudentMark : eStudentMarks) {
                estudentMarkService.delEStudentMark(eStudentMark);
            }
            resultMap.put("success", true);
            resultMap.put("msg", "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "删除失败!" + e.getMessage());
        }
        return resultMap;
    }


}
