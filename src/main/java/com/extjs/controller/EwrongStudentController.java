package com.extjs.controller;

import com.extjs.model.EWrongStudentDTO;
import com.extjs.service.EtestpaperService;
import com.extjs.service.EwrongStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jenny on 2017/5/22.
 */
@Controller
@RequestMapping("ewrongStudent")
public class EwrongStudentController {
    @Autowired
    private EwrongStudentService ewrongStudentService;
    @Autowired
    private EtestpaperService etestpaperService;

    @RequestMapping("/viewWrongStudent")
    @ResponseBody
    public Map<String, Object> queryWrongStudent(String countryid, String tpno) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {

            EWrongStudentDTO wrongStudentDTO = new EWrongStudentDTO();
            wrongStudentDTO.setCountryid(countryid.trim());
            wrongStudentDTO.setTestpaperno(tpno.trim());
            List<EWrongStudentDTO> wrongStudentDTOS = ewrongStudentService.queryWrongStudent(wrongStudentDTO);
            resultMap.put("data", wrongStudentDTOS);
            resultMap.put("total", wrongStudentDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 查询出试卷小题总数（要求数据库中试卷id存储为顺序整形），拼装为"学生错题记录"集合，针对已登记错题，设置estate为true（页面显示为选中）
     * 待解决：将学生错题记录表中的questionno设置为int
     *
     * @param tpno
     * @param countryid
     * @param testdate
     * @return
     */
    @RequestMapping("/getQuestionNumList")
    @ResponseBody
    public Map<String, Object> getQuestionNumList(String tpno, String countryid, String testdate) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer num = etestpaperService.getSumQuestionNum(tpno);
        List<EWrongStudentDTO> eWrongStudentDTOList = new ArrayList<EWrongStudentDTO>();
        HashMap<Integer, Integer> questionnoMap = ewrongStudentService.getQuestionno(countryid);
        try {
            for (int i = 0; i < num; i++) {
                EWrongStudentDTO wrongStudentDTO = new EWrongStudentDTO();
                wrongStudentDTO.setTestpaperno(tpno);
                wrongStudentDTO.setCountryid(countryid);
                Date date = new Date(System.currentTimeMillis());
                wrongStudentDTO.setTestdate(date);
                wrongStudentDTO.setQuestionno(i + 1);

                if (null != questionnoMap.get(i + 1)) {
                    wrongStudentDTO.setEstate(true);
                }
                eWrongStudentDTOList.add(wrongStudentDTO);
            }
            resultMap.put("data", eWrongStudentDTOList);
            resultMap.put("total", eWrongStudentDTOList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @RequestMapping("/modifWrongStudent")
    @ResponseBody
    public Map<String, Object> modifWrongStudent(@RequestBody List<EWrongStudentDTO> wrongStudentDTOS) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String str = ewrongStudentService.modifdSelected(wrongStudentDTOS);
            resultMap.put("success", true);
            resultMap.put("msg", "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "删除失败!" + e.getMessage());
        }

        return resultMap;
    }

    @RequestMapping("/delWrongStudent")
    @ResponseBody
    public Map<String, Object> delWrongStudent(String countryid, String tpno) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            EWrongStudentDTO eWrongStudentDTO = new EWrongStudentDTO();
            eWrongStudentDTO.setCountryid(countryid);
            eWrongStudentDTO.setTestpaperno(tpno);
            ewrongStudentService.delWrongStudent(eWrongStudentDTO);
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
