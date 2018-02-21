package com.extjs.service.impl;

import com.extjs.dao.EStudentMarkDao;
import com.extjs.model.*;
import com.extjs.service.EstudentMarkService;
import com.extjs.service.EsubjectsService;
import com.extjs.service.EtestpaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by jenny on 2017/5/12.
 */
@Service
@Scope("prototype")
public class EstudentMarkServiceImpl implements EstudentMarkService {
    @Autowired
    private EStudentMarkDao studentMarkDao;
    @Autowired
    private EtestpaperService etestpaperService;
    @Autowired
    private EsubjectsService esubjectsService;

    @Override
    public List<EStudentMark> queryEStudentMark(EStudentMark eStudentMark, Page page) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
//        Date date = new Date(System.currentTimeMillis());
        eStudentMark.setCreator(userDetails.getUsername());
//        eStudentMark.setCreatedate(date);
        List<EStudentMark> studentMarks = studentMarkDao.queryEStudentMark(eStudentMark, page);
        return studentMarks;
    }

    @Override
    public List<EStudentMark> getAverageMark(EStudentMark eStudentMark, String gradeno) {
        List<EStudentMark> studentMarks = studentMarkDao.getAverageMark(eStudentMark, gradeno);
        return studentMarks;
    }

    @Override
    public Float getAvgMarkByStudent(String studentID, String testDate, String subjectno) {
        Float res=studentMarkDao.getAvgMarkByStudent(studentID, testDate, subjectno);
        return res;
    }

    @Override
    public LinkedHashMap<String,Float> getAvgMarkByClass(String classno, String year) {

        LinkedHashMap<String, Float> map = studentMarkDao.getAvgMarkByClass(classno, year);

        return map;
    }

    @Override
    public LinkedHashMap<String,Float> getAvgMarkByGrade(String gradeno, String year) {
        LinkedHashMap<String, Float> map = studentMarkDao.getAvgMarkByGrade(gradeno, year);

        return map;
    }

    @Override
    public int getMarkAreaNum(String tpno, String classno, String markArea) {
        int res = studentMarkDao.getMarkAreaNum(tpno, classno, markArea);
        return res;
    }

    @Override
    public Float getAvgMark(String classno, String testDate, String subjectno) {
        Float res = studentMarkDao.getAvgMark(classno, testDate, subjectno);
        return res;
    }

    @Override
    public Float getAvgMiddleOrFinal(String classno, String year, String subjectno, String examType) {
        Float res = studentMarkDao.getAvgMiddleOrFinal(classno, year, subjectno, examType);
        return res;
    }

    @Override
    public Float getAvgMiddleOrFinalByStudent(String studentID, String year, String subjectno, String examtype) {
        return studentMarkDao.getAvegMiddleOrFinalByStudent(studentID, year, subjectno, examtype);
    }

    @Override
    public int getMareAreaTotalNum(String classno, String markArea, String tpnoString) {
        int res = studentMarkDao.getMarkAreaTotalNum(classno, markArea, tpnoString);
        return res;
    }

    @Override
    public HashMap<String, Integer> getStudentNum() {
        HashMap<String, Integer> map = studentMarkDao.getStudentNum();
        return map;
    }

    /**
     * id为空说明要执行新增或未取得已有记录的id值，根据paperid、studentno（联合主键）查询是否存在记录，不存在-生成id新增，存在-取已有id赋值更新
     *
     * @param eStudentMark
     */
    @Override
    public void modifEStudentMark(EStudentMark eStudentMark) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Date date = new Date(System.currentTimeMillis());
        eStudentMark.setCreator(userDetails.getUsername());
        eStudentMark.setCreatedate(date);
        if (null == eStudentMark.getMarktwo() || "".equals(eStudentMark.getMarktwo())) {
            eStudentMark.setMarktwo((float) 0);
        }
        if (null == eStudentMark.getMarkone() || "".equals(eStudentMark.getMarkone())) {
            eStudentMark.setMarkone((float) 0);
        }
        eStudentMark.setMark(eStudentMark.getMarkone() + eStudentMark.getMarktwo());
        if (null != eStudentMark.getTpno() && eStudentMark.getTpno().length() > 0) {//获取试卷名称、考试日期
            ETestpaperDTO testpaperDTO = new ETestpaperDTO();
            testpaperDTO.setTpno(eStudentMark.getTpno());
            List<ETestpaperDTO> testpaperDTOList = etestpaperService.queryEtestpaper(testpaperDTO,null);
            for (ETestpaperDTO eTestpaperDTO : testpaperDTOList) {
                eStudentMark.setTpname(eTestpaperDTO.getTpname());
                eStudentMark.setTestdate(eTestpaperDTO.getTestdate());
            }
        }


//        if (null == eStudentMark.getId() || eStudentMark.getId().length() == 0) {
        List<EStudentMark> studentMarkLis = this.queryEStudentMark(eStudentMark, new Page(0, 0));
        if (studentMarkLis.size() == 0) {
            UUID uuid = UUID.randomUUID();
            eStudentMark.setId(uuid.toString());

        } else {
            for (EStudentMark studentMark : studentMarkLis) {
                eStudentMark.setId(studentMark.getId());
            }
        }
        studentMarkDao.saveOrUpdateEStudentMark(eStudentMark);
    }
//    }

    @Override
    public void modifOnlyMark(EStudentMark studentMark) {
        studentMarkDao.modifOnlyMark(studentMark);
    }

    @Override
    public void delEStudentMark(EStudentMark eStudentMark) {
        studentMarkDao.delEStudentMark(eStudentMark);
    }

    @Override
    public void delEStudentMark() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
//        Date date = new Date(System.currentTimeMillis());
        studentMarkDao.delEStudentMarkByCreator(userDetails.getUsername());
    }

    @Override
    public int getTotalCount(EStudentMark eStudentMark) {
        int res = studentMarkDao.getTotalCount(eStudentMark);
        return res;
    }
}
