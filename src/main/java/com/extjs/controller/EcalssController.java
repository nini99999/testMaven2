package com.extjs.controller;

import com.extjs.model.EClassDTO;
import com.extjs.model.ESchoolDTO;
import com.extjs.model.ETeacherClassDTO;
import com.extjs.service.EclassService;
import com.extjs.service.EschoolService;
import com.extjs.service.EteacherClassService;
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

import java.util.*;

/**
 * Created by jenny on 2017/3/22.
 */
@Controller
@RequestMapping("eclass")
public class EcalssController {
    @Autowired
    private EclassService eclassService;

    @Autowired
    private EteacherClassService eteacherClassService;

    @Autowired
    private EschoolService eschoolService;

    @RequestMapping("/viewEclassByDTO")
    @ResponseBody
    public Map queryEclassByDTO(EClassDTO eClassDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<EClassDTO> eClassDTOList = new ArrayList<EClassDTO>();

        if (null != eClassDTO.getGradeno() && "noselected".equals(eClassDTO.getGradeno())) {
            eClassDTO.setGradeno(null);
        }

        if (null == eClassDTO) {
            eClassDTOList = eclassService.queryEclass();
        } else {
            eClassDTOList = eclassService.queryEclassByDTO(eClassDTO);
        }
        resultMap.put("data", eClassDTOList);
        resultMap.put("total", eClassDTOList.size());
        return resultMap;
    }

    /**
     * TODO:根据教师id（teacherid）判断页面（viewEteacher.jsp中的modal窗体）显示时，checkbox状态是否为选中
     * 1.根据教师id查询出其授课的班级
     * 2.循环指定学校的所有班级列表classlist，判断classno在list中是否非空，如有值则设置estate值为true（checkbox为选中状态）
     * 返回Map
     */

    @RequestMapping("/viewEclassEstate")
    @ResponseBody
    public Map queryEclassEstate(EClassDTO eClassDTO) {
        ETeacherClassDTO eTeacherClassDTO = new ETeacherClassDTO();
        eTeacherClassDTO.setTeacherno(eClassDTO.getTeacherno());
        List<ETeacherClassDTO> eTeacherClassDTOList = eteacherClassService.queryETeacherClass(eTeacherClassDTO);//根据教师id查询其授课的班级list
        HashMap<String, String> map = new HashMap<String, String>();
        for (ETeacherClassDTO eTeacherClassDTO1 : eTeacherClassDTOList) {
            map.put(eTeacherClassDTO1.getClassno(), eTeacherClassDTO1.getTeacherno());
        }
        if (null == eClassDTO.getSchoolno() || eClassDTO.getSchoolno().length() == 0) {
            try {
                eClassDTO.setSchoolno(eschoolService.getSchoolnoByContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> resultMap = this.queryEclassByDTO(eClassDTO);//查询指定学校下属班级
        List<EClassDTO> eClassDTOList = (List<EClassDTO>) resultMap.get("data");//强制类型转化获取班级list
        List<EClassDTO> resulteClassDTOList = new ArrayList<EClassDTO>();
        for (EClassDTO eClassDTO1 : eClassDTOList) {

            if (null != map.get(eClassDTO1.getClassno())) {
                eClassDTO1.setEstate(true);

            }
            resulteClassDTOList.add(eClassDTO1);
        }
        resultMap = new HashMap<String, Object>();
        resultMap.put("data", resulteClassDTOList);
        resultMap.put("total", resulteClassDTOList.size());
        return resultMap;
    }

    @RequestMapping(value = "/addEclass", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addElclass(EClassDTO eClassDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            eclassService.addEclass(eClassDTO);
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/delEclass", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delElclass(@RequestBody List<EClassDTO> eClassDTOList) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            for (EClassDTO eClassDTO : eClassDTOList) {
                eclassService.delEclass(eClassDTO);
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
