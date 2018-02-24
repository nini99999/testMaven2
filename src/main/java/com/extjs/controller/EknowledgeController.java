package com.extjs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.extjs.model.EKnowledge;
import com.extjs.service.EKnowledgeService;
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
@RequestMapping("knowledge")
public class EknowledgeController {
    @Autowired
    private EKnowledgeService eKnowledgeService;

    @Autowired
    private EQuestionKnowledgeService eQuestionKnowledgeService;

    @RequestMapping("/getTree")
    @ResponseBody
    public Map getKnowledgeTree(String id, String questionID) {
        Map resultMap = new HashMap();
        List<EKnowledge> knowledges = eKnowledgeService.getKnowledgeContainsChilds(id);
        List<EKnowledge> eKnowledges = eQuestionKnowledgeService.knowledgeCheckedOrNot(questionID, knowledges);
        resultMap.put("data", eKnowledges);
        resultMap.put("total", eKnowledges.size());
        return resultMap;
    }

    @RequestMapping("/getTreeWithGrasping")
    @ResponseBody
    public Map getTreeWithGrasping(String studentID, String rootID) {
        Map resultMap = new HashMap();
        try {
            List<EKnowledge> knowledges = eKnowledgeService.getKnowledgeGrasping(studentID, rootID);
            resultMap.put("data",knowledges);
            resultMap.put("total",knowledges.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
//    @RequestMapping("/getNextChilds")
//    @ResponseBody
//    public List<EKnowledge> getNextChilds(String parentID) {
//        EKnowledge knowledge = new EKnowledge();
//        knowledge.setParentid(parentID);
//        List<EKnowledge> knowledges = eKnowledgeService.queryKnowledge(knowledge);
//        return knowledges;
//
//    }

    @RequestMapping("/saveKnowledgeText")
    @ResponseBody
    public Map saveKnowledge(String id, String knowledgeText) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            eKnowledgeService.modifKnowledgeTextOnly(id, knowledgeText);
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping("/getKnowledge")
    @ResponseBody
    public List<EKnowledge> getKonwledge(String id) {
        EKnowledge knowledge = new EKnowledge();
        knowledge.setId(id);
        List<EKnowledge> eKnowledges = eKnowledgeService.queryKnowledge(knowledge);
        return eKnowledges;
    }

    @RequestMapping("/addKnowledge")
    @ResponseBody
    public Map addKnowledge(EKnowledge eknowledge) {
        Map resultMap = new HashMap();
        EKnowledge knowledge;
        try {
            knowledge = eKnowledgeService.addKnowledge(eknowledge);
            resultMap.put("data", knowledge);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("msg", e.toString());
        }

        return resultMap;
    }

    @RequestMapping("/delKnowledge")
    @ResponseBody
    public Map delKnowledge(String id) {
        Map resultMap = new HashMap();
        try {
            if (null != id && id.length() > 0) {
                eKnowledgeService.delKnowledge(id);
                resultMap.put("success", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
        }
        return resultMap;
    }

    @RequestMapping("/getOrCreatRoot")
    @ResponseBody
    public Map getOrCreateRoot(String gradeno, String subjectno) {
        Map resultMap = new HashMap();
        try {
            EKnowledge knowledge = eKnowledgeService.createRootNode("root-" + gradeno + subjectno, gradeno, subjectno);
            resultMap.put("data", knowledge);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
