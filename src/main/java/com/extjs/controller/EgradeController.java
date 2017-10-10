package com.extjs.controller;

import com.extjs.dao.EschoolDao;
import com.extjs.model.EGradeDTO;
import com.extjs.model.ESchool;
import com.extjs.model.UserDTO;
import com.extjs.service.EgradeService;
import com.extjs.service.EschoolService;
import com.extjs.service.UserService;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
 * Created by jenny on 2017/3/20.
 */
@Controller
@RequestMapping("egrade")

public class EgradeController {
    @Autowired
    private EgradeService egradeService;
    @Autowired
    private UserService userService;
    @Autowired
    private EschoolDao eschoolDao;

    @RequestMapping("/viewEgradeList")
    @ResponseBody
    public Map queryEgrade() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<EGradeDTO> eGradeDTOList = egradeService.queryEgrade();
        resultMap.put("data", eGradeDTOList);
        resultMap.put("total", eGradeDTOList.size());
        return resultMap;
    }

    @RequestMapping("/viewEgradeListByschoolno")
    @ResponseBody
    public Map queryEgradeByschoolno(String schoolno) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<EGradeDTO> eGradeDTOList = new ArrayList<EGradeDTO>();

        eGradeDTOList = egradeService.queryEgradeByschoolno(schoolno);

        resultMap.put("data", eGradeDTOList);
        resultMap.put("total", eGradeDTOList.size());
        return resultMap;
    }

    @RequestMapping(value = "/addEgrade", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addEgrade(EGradeDTO eGradeDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        eGradeDTO.setGradeno(eGradeDTO.getSchoolno() + "-" + eGradeDTO.getGradeno());
        try {
            egradeService.addEgrade(eGradeDTO);
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/delEgrade", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delEgrade(@RequestBody List<EGradeDTO> eGradeDTOList) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            for (EGradeDTO eGradeDTO : eGradeDTOList) {
                egradeService.delEgrade(eGradeDTO);
            }
            resultMap.put("success", true);
            resultMap.put("msg", "删除成功!");

        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("msg", "删除失败!" + e.getMessage());
        }
        return resultMap;
    }
}
