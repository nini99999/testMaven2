package com.extjs.controller;

import com.extjs.model.EClassDTO;
import com.extjs.model.ETeacherClassDTO;
import com.extjs.model.ETeacherDTO;
import com.extjs.model.EteacherClassVO;
import com.extjs.service.EteacherClassService;
import com.extjs.service.EteacherService;
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
 * Created by jenny on 2017/3/27.
 */
@Controller
@RequestMapping("eteacher")
public class EteacherController {
    @Autowired
    private EteacherService eteacherService;

    @Autowired
    private EteacherClassService eteacherClassService;

    /**
     * 查询教师信息
     *
     * @param eTeacherDTO
     * @return
     */
    @RequestMapping("/viewEteacherByDTO")
    @ResponseBody
    public Map<String, Object> queryEteacher(ETeacherDTO eTeacherDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<ETeacherDTO> eTeacherDTOList = new ArrayList<ETeacherDTO>();
        if (null != eTeacherDTO.getSchoolno() && "noselected".equals(eTeacherDTO.getSchoolno())) {
            eTeacherDTO.setSchoolno(null);
        }
        eTeacherDTOList = eteacherService.queryEteacher(eTeacherDTO);
        resultMap.put("data", eTeacherDTOList);
        resultMap.put("total", eTeacherDTOList.size());
        return resultMap;
    }

    /**
     * 添加教师信息
     *
     * @param eTeacherDTO
     * @return
     */
    @RequestMapping(value = "/addEteacher", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addEteacher(ETeacherDTO eTeacherDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            eteacherService.addEteacher(eTeacherDTO);
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }
        return resultMap;
    }


    /**
     * 删除教师信息-级联删除，应该将业务功能引入service中实现，删除事务需引入注解，待完成
     *
     * @param eTeacherDTOList
     * @return
     */
    @RequestMapping(value = "/delEteacher", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delEteacher(@RequestBody List<ETeacherDTO> eTeacherDTOList) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            for (ETeacherDTO eTeacherDTO : eTeacherDTOList) {
                eteacherClassService.delETeacherClassByTeacherid(eTeacherDTO.getTeacherid());//首先删除教师-授课班级对应关系表中相应数据
                eteacherService.delEteacher(eTeacherDTO);
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

    /**
     * 添加选中行的班级（教师授课班级），添加前首先根据teacherid删除对应关系表中的相关数据；
     * -构建了视图类EteacherClassVO，用于接收页面传递的参数：teacherid+ String[] classno（班级编码列表）
     *
     * @param eTeacherClassDTOList
     * @return
     */
    @RequestMapping(value = "/addEteacherClass", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addEteacherClass(@RequestBody EteacherClassVO eteacherClassVO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        ETeacherClassDTO eTeacherClassDTO = new ETeacherClassDTO();
        List<ETeacherClassDTO> eTeacherClassDTOList = new ArrayList<ETeacherClassDTO>();
        String teacherid = eteacherClassVO.getTeacherid();
        String[] classno = eteacherClassVO.getClassno();
        for (int i = 0; i < classno.length; i++) {
            eTeacherClassDTO = new ETeacherClassDTO();
            eTeacherClassDTO.setTeacherno(teacherid);
            eTeacherClassDTO.setClassno(classno[i]);
            eTeacherClassDTOList.add(eTeacherClassDTO);
        }
        try {
            eteacherClassService.delETeacherClassByTeacherid(teacherid);//根据指定teacherid删除原有对应班级
            eteacherClassService.addETeacherClass(eTeacherClassDTOList);//批量添加
            resultMap.put("success", true);
            resultMap.put("msg", "保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "删除失败!" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/getEteacherClass")
    @ResponseBody
    public Map getEteacherClass(String strParentID) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<ETeacherClassDTO> eTeacherClassDTOList = new ArrayList<ETeacherClassDTO>();
        ETeacherClassDTO eTeacherClassDTO = new ETeacherClassDTO();
        eTeacherClassDTO.setTeacherno(strParentID);
        eTeacherClassDTOList = eteacherClassService.queryETeacherClass(eTeacherClassDTO);
        resultMap.put("data", eTeacherClassDTOList);
        resultMap.put("total", eTeacherClassDTOList.size());
        return resultMap;
    }
}
