package com.extjs.controller;

import com.extjs.model.ESchoolDTO;
import com.extjs.service.EschoolService;
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
 * Created by jenny on 2017/3/19.
 */

@Controller
@RequestMapping("eschool")

public class EschoolController {
    @Autowired
    private EschoolService eschoolService;


    @RequestMapping("/viewEschoolList")
    @ResponseBody
    public Map queryEschoolList() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<ESchoolDTO> eSchoolDTOList = eschoolService.queryEschool();
        resultMap.put("data", eSchoolDTOList);
        resultMap.put("total", eSchoolDTOList.size());
        return resultMap;
    }


    @RequestMapping(value = "/addEshool", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addEschool(ESchoolDTO eSchoolDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            eschoolService.addEschool(eSchoolDTO);//添加学校信息
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/delEshool", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delEschool(@RequestBody List<ESchoolDTO> eSchoolDTOList) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            for (ESchoolDTO eSchoolDTO : eSchoolDTOList) {
                eschoolService.delEschool(eSchoolDTO);

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
