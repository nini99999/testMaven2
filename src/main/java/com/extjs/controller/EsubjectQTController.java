package com.extjs.controller;

import com.extjs.model.ESubjectQTypeDTO;
import com.extjs.service.EsubjectQTypeService;
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
 * Created by jenny on 2017/4/2.
 */
@Controller
@RequestMapping("esubjectqt")
public class EsubjectQTController {
    @Autowired
    private EsubjectQTypeService esubjectQTypeService;


    @RequestMapping("/viewEsubjectQT")
    @ResponseBody
    public Map queryEsubjectQT(String subjectno) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<ESubjectQTypeDTO> eSubjectQTypeDTOList = new ArrayList<ESubjectQTypeDTO>();
        ESubjectQTypeDTO eSubjectQTypeDTO = new ESubjectQTypeDTO();
        if (null != subjectno && !"".equals(subjectno)) {

            eSubjectQTypeDTO.setSubjectno(subjectno);

        }
        eSubjectQTypeDTOList = esubjectQTypeService.queryESubjectQT(eSubjectQTypeDTO);
        resultMap.put("data", eSubjectQTypeDTOList);
//        resultMap.put("rows",eSubjectQTypeDTOList);
        resultMap.put("total", eSubjectQTypeDTOList.size());
        return resultMap;
    }


    @RequestMapping(value = "/addEsubjectQT", method = RequestMethod.POST)
    @ResponseBody
    public Map addEsubjectQT(ESubjectQTypeDTO eSubjectQTypeDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        eSubjectQTypeDTO.setQuestiontype(eSubjectQTypeDTO.getSubjectno() + "-" + eSubjectQTypeDTO.getQuestiontype());
        try {
            esubjectQTypeService.addESubjectQT(eSubjectQTypeDTO);
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/delEsubjectQT", method = RequestMethod.POST)
    @ResponseBody
    public Map delEsubjectQT(@RequestBody List<ESubjectQTypeDTO> eSubjectQTypeDTOList) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            for (ESubjectQTypeDTO eSubjectQTypeDTO:eSubjectQTypeDTOList){
                esubjectQTypeService.delESubjectQT(eSubjectQTypeDTO);
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
