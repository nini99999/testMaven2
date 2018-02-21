package com.extjs.controller;

import com.extjs.dao.EpaperQTypeDao;
import com.extjs.model.EPaperQTypeDTO;
import com.extjs.model.ETestpaperDTO;
import com.extjs.model.Page;
import com.extjs.service.EpaperQTypeService;
import com.extjs.service.EtestpaperService;
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

import com.extjs.util.EConstants;

/**
 * Created by jenny on 2017/4/4.
 */
@Controller
@RequestMapping("etestpaper")
public class EtestpaperController {
    @Autowired
    private EtestpaperService etestpaperService;
    @Autowired
    private EpaperQTypeService epaperQTypeService;

    @RequestMapping("/getTermList")
    @ResponseBody
    public Map getTermList() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        EConstants eConstants = new EConstants();
        resultMap.put("term", eConstants.termMap);
        return resultMap;
    }

    @RequestMapping("/getExamType")
    @ResponseBody
    public Map getExamType() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        EConstants eConstants = new EConstants();
        resultMap.put("examtype", eConstants.examType);
        return resultMap;
    }

    @RequestMapping("/getPaperQType")
    @ResponseBody
    public Map getPaperQType(String strParentID) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<EPaperQTypeDTO> ePaperQTypeDTOList = new ArrayList<EPaperQTypeDTO>();
        EPaperQTypeDTO ePaperQTypeDTO = new EPaperQTypeDTO();
        ePaperQTypeDTO.setTpno(strParentID);
        ePaperQTypeDTOList = epaperQTypeService.queryEpaperQType(ePaperQTypeDTO);
        resultMap.put("data", ePaperQTypeDTOList);
        resultMap.put("total", ePaperQTypeDTOList.size());
        return resultMap;
    }

    @RequestMapping("/viewTestPaper")
    @ResponseBody
    public Map queryTestPaper(ETestpaperDTO eTestpaperDTO, Page page) {
        if ("noselected".equals(eTestpaperDTO.getGradeno())) {
            eTestpaperDTO.setGradeno(null);
        }
        String startDate;
        String endDate;
        if (null != eTestpaperDTO.getStartDate() && eTestpaperDTO.getStartDate().length() > 0) {
            startDate = eTestpaperDTO.getStartDate().substring(0, 8);
            eTestpaperDTO.setStartDate(startDate);
        }
        if (null != eTestpaperDTO.getEndDate() && eTestpaperDTO.getEndDate().length() > 0) {
            endDate = eTestpaperDTO.getEndDate().substring(eTestpaperDTO.getEndDate().length() - 8, eTestpaperDTO.getEndDate().length());
            eTestpaperDTO.setEndDate(endDate);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<ETestpaperDTO> eTestpaperDTOList = new ArrayList<ETestpaperDTO>();
        eTestpaperDTOList = etestpaperService.queryEtestpaper(eTestpaperDTO, page);
        int totalCount = etestpaperService.getTotalCount(eTestpaperDTO);
        resultMap.put("rows", eTestpaperDTOList);
        resultMap.put("total", totalCount);
        return resultMap;
    }

    @RequestMapping(value = "/addTestPaper", method = RequestMethod.POST)
    @ResponseBody
    public Map addTestPaper(ETestpaperDTO eTestpaperDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            etestpaperService.addEtestpaper(eTestpaperDTO);
            resultMap.put("success", true);
            resultMap.put("msg", "保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "保存失败!" + e.getMessage());
        }
        return resultMap;
    }

    /**
     * 删除试卷表，同步删除子表
     *
     * @param eTestpaperDTOList
     * @return
     */
    @RequestMapping(value = "/delTestPaper", method = RequestMethod.POST)
    @ResponseBody
    public Map delTestPaper(@RequestBody List<ETestpaperDTO> eTestpaperDTOList) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            EPaperQTypeDTO ePaperQTypeDTO;
            for (ETestpaperDTO eTestpaperDTO : eTestpaperDTOList) {
                etestpaperService.delEtestpaper(eTestpaperDTO);//删除主表
                epaperQTypeService.delEpaperQType(eTestpaperDTO.getTpno());//根据试卷编码删除子表
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
     * 添加或修改，试卷题型对应表，
     *
     * @param ePaperQTypeDTO
     * @return
     */
    @RequestMapping(value = "/modifPaperQT", method = RequestMethod.POST)
    @ResponseBody
    public Map modifPaperQType(EPaperQTypeDTO ePaperQTypeDTO) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            epaperQTypeService.addEpaperQType(ePaperQTypeDTO);

            List<EPaperQTypeDTO> ePaperQTypeDTOList = new ArrayList<EPaperQTypeDTO>();

            ePaperQTypeDTOList = epaperQTypeService.queryEpaperQType(ePaperQTypeDTO);
            resultMap.put("data", ePaperQTypeDTOList);//重新查询子表数据，页面中进行重新绑定刷新
            resultMap.put("success", true);
            resultMap.put("msg", "保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "保存失败!" + e.getMessage());
        }
        return resultMap;

    }
}