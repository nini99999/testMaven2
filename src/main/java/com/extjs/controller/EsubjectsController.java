package com.extjs.controller;

import com.extjs.model.ESubjectQTypeDTO;
import com.extjs.model.ESubjectQTypeVO;
import com.extjs.model.EsubjectsDTO;
import com.extjs.service.EsubjectsService;
import com.extjs.util.SysException;
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
 * Created by jenny on 2017/3/16.
 */

@Controller
@RequestMapping("esubjects")

public class EsubjectsController {
    @Autowired
    private EsubjectsService esubjectsService;

    /**
     * 查询学科信息
     * @return
     */
    @RequestMapping("/viewEsubjectsList")
    @ResponseBody
    public Map queryEsbujectsList() {//调用service进行查询，返回结果为Map型
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<EsubjectsDTO> esubjectsDTOList = esubjectsService.queryEsubjectsList();
        resultMap.put("data", esubjectsDTOList);
        resultMap.put("total", esubjectsDTOList.size());
        System.out.print(resultMap);
        return resultMap;


    }

    /**
     * 添加学科
     * @param esubjectsDTO
     * @return
     */
    @RequestMapping(value = "/addEsubjects", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addEsubjects(EsubjectsDTO esubjectsDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            esubjectsService.addEsubjects(esubjectsDTO);
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
     * 删除学科，级联删除学科题型数据；
     * @param esubjectsDTOList
     * @return
     */
    @RequestMapping(value = "/delsubjects", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delEsubjects(@RequestBody List<EsubjectsDTO> esubjectsDTOList) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {

            for (EsubjectsDTO esubjectsDTO : esubjectsDTOList) {
                esubjectsService.delEsubjects(esubjectsDTO);

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
     * 添加选中学科行、选中学科题型checkbox数据
     * @param eSubjectQTypeVO
     * @return

    @RequestMapping(value = "/addESubjectQT", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addESubjectQT(@RequestBody ESubjectQTypeVO eSubjectQTypeVO){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<ESubjectQTypeDTO> eSubjectQTypeDTOList=new ArrayList<ESubjectQTypeDTO>();
        ESubjectQTypeDTO eSubjectQTypeDTO=new ESubjectQTypeDTO();
        String subjectno=eSubjectQTypeVO.getSubjectno();
        String[] vos=eSubjectQTypeVO.getQuestionType();
        for (int i=0;i<vos.length;i++){
            eSubjectQTypeDTO=new ESubjectQTypeDTO();
            eSubjectQTypeDTO.setSubjectno(subjectno);
            eSubjectQTypeDTO.setQuestiontype(vos[i]);
            eSubjectQTypeDTOList.add(eSubjectQTypeDTO);
        }

    } */
}
