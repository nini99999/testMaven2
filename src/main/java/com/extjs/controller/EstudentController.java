package com.extjs.controller;

import com.extjs.util.EConstants;

import com.extjs.model.EStudentDTO;
import com.extjs.service.EstudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jenny on 2017/3/24.
 */
@Controller
@RequestMapping("estudent")
public class EstudentController {
    @Autowired
    private EstudentService estudentService;

    @RequestMapping("/viewEstudentByDTO")
    @ResponseBody
    public Map queryEstudentByDTO(EStudentDTO eStudentDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<EStudentDTO> eStudentDTOList = new ArrayList<EStudentDTO>();
        if (null != eStudentDTO.getSchoolno() && "noselected".equals(eStudentDTO.getSchoolno())) {
            eStudentDTO.setSchoolno(null);
        }
        if (null != eStudentDTO.getGradeno() && "noselected".equals(eStudentDTO.getGradeno())) {
            eStudentDTO.setGradeno(null);
        }
        if (null != eStudentDTO.getClassno() && "noselected".equals(eStudentDTO.getClassno())) {
            eStudentDTO.setClassno(null);
        }

        eStudentDTOList = estudentService.queryEstudent(eStudentDTO);
        resultMap.put("data", eStudentDTOList);
        resultMap.put("total", eStudentDTOList.size());
        resultMap.put("rows", eStudentDTOList);
        return resultMap;
    }

    @RequestMapping("/getStudentByClassAndTpno")
    @ResponseBody
    public Map getStudentByClassAndTpno(String classno, String tpno) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<EStudentDTO> eStudentDTOList = new ArrayList<EStudentDTO>();
        eStudentDTOList = estudentService.getStudentByClassAndTpno(classno, tpno);
        resultMap.put("data", eStudentDTOList);
        resultMap.put("total", eStudentDTOList.size());
        resultMap.put("rows", eStudentDTOList);
        return resultMap;
    }

    @RequestMapping(value = "/addEstudent", method = RequestMethod.POST)
    @ResponseBody
    public Map addEstudent(EStudentDTO eStudentDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            estudentService.addEstudent(eStudentDTO);
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加学生失败!" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/delEstudent", method = RequestMethod.POST)
    @ResponseBody
    public Map delEstudent(@RequestBody List<EStudentDTO> eStudentDTOList) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            for (EStudentDTO eStudentDTO : eStudentDTOList) {
                estudentService.delEstudent(eStudentDTO);
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


    @RequestMapping("/getNation")
    @ResponseBody
    public Map getNation() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        EConstants eConstants = new EConstants();
        resultMap.put("schoolstate", eConstants.schoolStateMap);
        resultMap.put("studystate", eConstants.studyStateMap);
        resultMap.put("nation", eConstants.nation);
        resultMap.put("total", eConstants.nation.length);
        return resultMap;

    }
}
