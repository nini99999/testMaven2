package com.extjs.controller;

import com.extjs.model.EKnowledge;
import com.extjs.model.EQuestionKnowledge;
import com.extjs.service.EQuestionKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("QuestionKnowledge")
public class EQuestionKnowledgeController {
    @Autowired
    private EQuestionKnowledgeService eQuestionKnowledgeService;

    @RequestMapping("/getList")
    @ResponseBody
    public List<EQuestionKnowledge> getQuestionKnowledges(String id) {
        List<EQuestionKnowledge> questionKnowledges = new ArrayList<>();
        EQuestionKnowledge questionKnowledge = new EQuestionKnowledge();
        questionKnowledge.setId(id);
        questionKnowledges = eQuestionKnowledgeService.getQuestionKnowledges(questionKnowledge);
        return questionKnowledges;
    }

    @RequestMapping("/addSelected")
    @ResponseBody
    public Map addSelected(String knowledgeIDS, String questionID) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            eQuestionKnowledgeService.addSelected(questionID, knowledgeIDS);
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }
        return resultMap;
    }
}
