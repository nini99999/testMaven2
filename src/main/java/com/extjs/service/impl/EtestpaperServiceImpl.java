package com.extjs.service.impl;

import com.extjs.dao.EpaperQTypeDao;
import com.extjs.dao.EtestpaperDao;
import com.extjs.model.*;
import com.extjs.service.EpaperQTypeService;
import com.extjs.service.EsubjectQTypeService;
import com.extjs.service.EtestpaperService;
import com.extjs.util.EConstants;
import com.extjs.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

/**
 * Created by jenny on 2017/4/4.
 */
@Service
@Scope("prototype")
public class EtestpaperServiceImpl implements EtestpaperService {
    @Autowired
    private EtestpaperDao etestpaperDao;
    @Autowired
    private EpaperQTypeService epaperQTypeService;
    @Autowired
    private EsubjectQTypeService esubjectQTypeService;

    @Override
    public List<ETestpaperDTO> queryEtestpaper(ETestpaperDTO eTestpaperDTO,Page page) {
        List<ETestpaperDTO> eTestpaperDTOList = new ArrayList<ETestpaperDTO>();
        List<ETestpaper> eTestpaperList = etestpaperDao.queryEtestPaper(eTestpaperDTO,page);
        EConstants eConstants = new EConstants();
        for (ETestpaper eTestpaper : eTestpaperList) {
//            eTestpaper.setExamtype(eConstants.examType.get(eTestpaper.getExamtype()));
            eTestpaperDTO = new ETestpaperDTO();
            ReflectionUtil.copyProperties(eTestpaper, eTestpaperDTO);
            eTestpaperDTO.setExamtypename(eConstants.examType.get(eTestpaperDTO.getExamtype()));
            eTestpaperDTOList.add(eTestpaperDTO);
        }
        return eTestpaperDTOList;
    }


    @Override
    public ETestpaperDTO getTestPaperByTPNO(String tpno) {
        ETestpaperDTO testpaperDTO=new ETestpaperDTO();
        ETestpaper testpaper=etestpaperDao.getTestPaper(tpno,null);
        ReflectionUtil.copyProperties(testpaper,testpaperDTO);

        return testpaperDTO;
    }

    @Override
    public Integer getSumQuestionNum(String tpno) {
        Integer integer = etestpaperDao.getSumQuestionNum(tpno);
        return integer;
    }

    @Override
    /**
     * 添加或修改
     * 添加时，自动添加指定学科下对应的所有题型至"试卷题型对应关系表"
     */
    public void addEtestpaper(ETestpaperDTO eTestpaperDTO) {
        Date date = new Date(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        eTestpaperDTO.setCreator(userDetails.getUsername());
        eTestpaperDTO.setCreatedate(date);

        if (null == eTestpaperDTO.getTpno() || "".equals(eTestpaperDTO.getTpno())) {//自动生成试卷编码
            eTestpaperDTO.setTpno(this.getTpno(eTestpaperDTO.getSchoolno(), eTestpaperDTO.getGradeno(), eTestpaperDTO.getSubjectno()));
        }

        if (null == eTestpaperDTO.getId() || "".equals(eTestpaperDTO.getId())) {//没有主表id，执行主表添加操作，首先添加子表信息
            UUID uuid = UUID.randomUUID();
            eTestpaperDTO.setId(uuid.toString());
            try {
                etestpaperDao.modifEtestPaper(eTestpaperDTO);//添加主表
                /**
                 * 添加子表，首先根据"学科题型表"查询出指定学科对应的所有题型，添加至"试题题型表"
                 */
                ESubjectQTypeDTO eSubjectQTypeDTO = new ESubjectQTypeDTO();
                eSubjectQTypeDTO.setSubjectno(eTestpaperDTO.getSubjectno());//指定查询条件学科编码：subjectno，获取所有题型
                List<ESubjectQTypeDTO> eSubjectQTypeDTOList = esubjectQTypeService.queryESubjectQT(eSubjectQTypeDTO);
                EPaperQTypeDTO ePaperQTypeDTO;
                List<EPaperQTypeDTO> ePaperQTypeDTOList = new ArrayList<EPaperQTypeDTO>();
                for (ESubjectQTypeDTO subjectQTypeDTO : eSubjectQTypeDTOList) {
                    ePaperQTypeDTO = new EPaperQTypeDTO();
                    ePaperQTypeDTO.setTpno(eTestpaperDTO.getTpno());
                    ePaperQTypeDTO.setQuestiontype(subjectQTypeDTO.getQuestiontype());
                    ePaperQTypeDTOList.add(ePaperQTypeDTO);

                }
                epaperQTypeService.addEpaperQTypeList(ePaperQTypeDTOList);//添加子表
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {//主表id存在，说明执行更新操作，仅更新主表信息，不对子表执行操作
            try {
                etestpaperDao.modifEtestPaper(eTestpaperDTO);//更新主表
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public HashMap<String, ETestpaperDTO> getEtestPaper() {
        HashMap resultMap = new HashMap<String, ETestpaperDTO>();
        ETestpaperDTO etestpaperDTO = new ETestpaperDTO();
        List<ETestpaperDTO> testpaperDTOList = this.queryEtestpaper(etestpaperDTO,null);
        for (ETestpaperDTO testpaperDTO : testpaperDTOList) {
            resultMap.put(testpaperDTO.getTpno(), testpaperDTO);
        }
        return resultMap;
    }

    /**
     * 添加时先自动生成试卷编码：tpno，规则：学校编码+年级编码+学科编码+系统日期+当天试卷记录的最大值
     *
     * @param school
     * @param grade
     * @param subject
     * @return tpno，自动生成的试卷编码
     */

    public String getTpno(String school, String grade, String subject) {
        String str = null;
        ETestpaperDTO eTestpaperDTO = new ETestpaperDTO();
        eTestpaperDTO.setSchoolno(school);
        eTestpaperDTO.setGradeno(grade);
        eTestpaperDTO.setSubjectno(subject);
        List<ETestpaperDTO> eTestpaperDTOList = this.queryEtestpaper(eTestpaperDTO,null);
        Date date = new Date(System.currentTimeMillis());
        str = grade + "#" + subject + "#" + date.toString() + "--" + String.valueOf(eTestpaperDTOList.size());
        return str;
    }

    @Override
    public void delEtestpaper(ETestpaperDTO eTestpaperDTO) {
        etestpaperDao.delEtestPaper(eTestpaperDTO);
    }

    @Override
    public int getTotalCount(ETestpaperDTO eTestpaperDTO) {
        int res=etestpaperDao.getTotalCount(eTestpaperDTO);
        return res;
    }
}
