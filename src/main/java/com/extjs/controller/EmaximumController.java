package com.extjs.controller;

import com.extjs.model.EQuestionMaximum;
import com.extjs.service.EQuestionMaximumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("maximum")
public class EmaximumController {
    @Autowired
    EQuestionMaximumService eQuestionMaximumService;

    @RequestMapping("/getMaximum")
    @ResponseBody
    public Map getMaximum(String id, String paperId, String questionTypeId) {
        Map resultMap = new HashMap();
        EQuestionMaximum questionMaximum = new EQuestionMaximum();
        questionMaximum.setPaperId(paperId);
        questionMaximum.setQuestionTypeId(questionTypeId);
        List<EQuestionMaximum> questionMaximums = eQuestionMaximumService.getQuestionMaximum(questionMaximum);
        resultMap.put("data", questionMaximums);
        resultMap.put("total", questionMaximums.size());
        return resultMap;
    }


    @RequestMapping("/addMaximums")
    @ResponseBody
    public Map<String, Object> addMaximums(String paperId, String questionTypeId, Integer questionNum, Float maximum) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            eQuestionMaximumService.addMaximums(paperId, questionTypeId, questionNum, maximum);

            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }


        return resultMap;
    }

    @RequestMapping("/updateMaximum")
    @ResponseBody

    public Map<String, Object> updateMaximum(String id, Float maximum) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            EQuestionMaximum questionMaximum = eQuestionMaximumService.getQuestionMaximum(id);
            questionMaximum.setMaximum(maximum);
            eQuestionMaximumService.modifQuestionMaximum(questionMaximum);
            resultMap.put("success", true);
            resultMap.put("msg", "修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "修改失败!" + e.getMessage());
        }


        return resultMap;
    }

    @RequestMapping(value = "/deleteMaximums", method = RequestMethod.POST)
    @ResponseBody

    public Map<String, Object> deleteMaximums(@RequestBody List<EQuestionMaximum> questionMaximums) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String paperId = "";
            String questionTypeId = "";
            for (EQuestionMaximum questionMaximum : questionMaximums) {
                eQuestionMaximumService.delQuestionMaximum(questionMaximum);
                paperId = questionMaximum.getPaperId();
                questionTypeId = questionMaximum.getQuestionTypeId();
            }
            resultMap.put("paperId", paperId);
            resultMap.put("questionTypeId", questionTypeId);
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
