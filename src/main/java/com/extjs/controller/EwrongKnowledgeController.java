package com.extjs.controller;

import com.extjs.model.EWrongKnowledgeDTO;
import com.extjs.service.EWrongKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("wrongKnowledge")
public class EwrongKnowledgeController {
    @Autowired
    EWrongKnowledgeService eWrongKnowledgeService;

    @RequestMapping("/getWrongKnowledges")
    @ResponseBody
    public Map getWrongKnowledges(EWrongKnowledgeDTO wrongKnowledgeDTO) {
        Map resultMap = new HashMap();
        try {
            List<EWrongKnowledgeDTO> wrongKnowledgeDTOS = eWrongKnowledgeService.getWrongKnowledges(wrongKnowledgeDTO);
            resultMap.put("data", wrongKnowledgeDTOS);
            resultMap.put("total", wrongKnowledgeDTOS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }
}
