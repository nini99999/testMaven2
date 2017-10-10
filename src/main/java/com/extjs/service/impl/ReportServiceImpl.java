package com.extjs.service.impl;

import com.extjs.dao.EStudentMarkDao;
import com.extjs.dao.EclassDao;
import com.extjs.dao.EstudentDao;
import com.extjs.dao.ReportDao;
import com.extjs.model.*;
import com.extjs.service.EclassService;
import com.extjs.service.ReportService;
import com.extjs.util.EConstants;
import com.extjs.util.SysException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by jenny on 2017/4/9.
 */
@Service
@Scope("prototype")
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private EclassDao eclassDao;
    @Autowired
    private EstudentDao estudentDao;

    @Override
    public List<RClassMark> queryRClassMark(RClassMark rClassMark) {
        List<RClassMark> rClassMarkList = reportDao.queryRClassMark(rClassMark);
        return rClassMarkList;
    }

    /**
     * 获取班级成绩计算数据
     * 删除当前用户存储在报表中的数据
     * 添加指定条件的数据至表中（指定学校、年级下所有班级、所有学科的）
     * 1、取班级列表，循环之
     * 2、查指定班级对应的成绩，形成class+mark+sum（mark）的数组
     * 3、添加数组至List中，
     * 4、结束循环，返回List
     *
     * @param gradeno
     * @return
     */
    @Override
    public List reckonClassMark(String gradeno) {
        RClassMark rClassMark;
        EClassDTO eClassDTO = new EClassDTO();
        eClassDTO.setGradeno(gradeno);

        List<EClass> eClassList = eclassDao.queryEclass(eClassDTO);//获取指定学校、年级所对应班级列表
        List resultList = new ArrayList();
        for (EClass eClass : eClassList) {
            rClassMark = new RClassMark();
            rClassMark.setClassno(eClass.getClassno());
            List<RClassMark> rClassMarkList = this.queryRClassMark(rClassMark);//查询指定班级对应的成绩数据
/**
 * 生成classno+Mark+sum（Mark）的组合数组String【】str，添加至List中
 */
            String[] str = new String[rClassMarkList.size() + 2];
            int i = 0;
            float sum = 0.0f;
            str[0] = eClass.getClassname();
//            str[0] = rClassMark.getClassno();
            for (RClassMark classMark : rClassMarkList) {
                i++;
                str[i] = String.valueOf(classMark.getMark());
                if (i == 1) {
                    sum = Float.parseFloat(str[i]);
                } else {
                    sum = Float.parseFloat(str[i]) + sum;
                }
            }
            DecimalFormat df = new DecimalFormat("##,###,###.##");
            String sdf = df.format(sum).toString();
            str[i + 1] = sdf;
            resultList.add(str);
        }
        return resultList;
    }

    @Override
    public void delRClassMark() throws SysException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        reportDao.delRClassMark(userDetails.getUsername());
    }

    @Override
    public List<RYearMark> queryRYearMark(String year, String gradeno, String subjectno, String studentno, String studentname) {
        List<RYearMark> rYearMarks = new ArrayList<RYearMark>();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        RYearMark rYearMark = new RYearMark();
        rYearMark.setCreator(userDetails.getUsername());
        rYearMark.setYear(year);
        rYearMark.setSubjectno(subjectno);
        if (null != studentno && !"".equals(studentno)) {//根据学籍号查询唯一学生记录；
            EStudent eStudent = new EStudent();
            eStudent = estudentDao.getEstudentByCountryID(studentno);
            rYearMark.setClassno(eStudent.getClassno());
        }
        rYearMarks = reportDao.queryRYearMark(rYearMark);
        return rYearMarks;
    }

    @Override
    public List<RYearMarkStudent> queryRYearMarkStudent(String year, String studentname, String subjectno, String studentno) {
        List<RYearMarkStudent> rYearMarkStudents = new ArrayList<RYearMarkStudent>();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        RYearMarkStudent rYearMarkStudent = new RYearMarkStudent();
        rYearMarkStudent.setCreator(userDetails.getUsername());
        rYearMarkStudent.setStudentname(studentname);
        rYearMarkStudent.setSubjectno(subjectno);
        rYearMarkStudent.setStudentno(studentno);
        rYearMarkStudents = reportDao.queryRYearMarkStudent(rYearMarkStudent);
        return rYearMarkStudents;
    }

    /**
     * 计算班级成绩分布
     * 1.根据creator删除表记录
     * 2.计算，并将结果insert至成绩分布表
     * 3.查询记录
     *
     * @param rMarkArea
     * @return
     */
    @Override
    public List<RMarkArea> queryRMarkArea(String gradeno, String subjectno) {
        List resultList = new ArrayList();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
//        reportDao.delRMarkArea(userDetails.getUsername());
        String[] cons;
        if (null != subjectno && !"".equals(subjectno)) {//判断是取总成绩还是取单科成绩：空-总成绩；非空-学科成绩

            cons = EConstants.subjectMarkArea;
        } else {
            cons = EConstants.markArea;
        }

        ArrayUtils.add(cons, 0, "班级");

//        resultList.add(cons);
        EClassDTO eClassDTO = new EClassDTO();
        eClassDTO.setGradeno(gradeno);
        List<EClass> eClassList = eclassDao.queryEclass(eClassDTO);//获取指定学校、年级所对应班级列表
        RMarkArea rMarkArea = new RMarkArea();
        for (EClass eClass : eClassList) {
            rMarkArea.setCreator(userDetails.getUsername());
            rMarkArea.setClassno(eClass.getClassno());
            rMarkArea.setSubjectno(subjectno);
            int sumStudent = 0;
            try {
                List<RMarkArea> rMarkAreaList = reportDao.queryRMarkArea(rMarkArea);//查询指定班级的成绩分布
                /**
                 * 生成classno+Mark+sum（MarkAreaNum）的组合数组String【】str，添加至List中
                 */
                String[] str = new String[cons.length + 2];
                str[0] = eClass.getClassname();
                int i = 0;
                for (RMarkArea area : rMarkAreaList) {
                    i++;
                    DecimalFormat df = new DecimalFormat("##,###,###");
                    str[i] = df.format(area.getMarkareanum()).toString();
                    if (i == 1) {
                        sumStudent = Integer.valueOf(str[1]);
                    } else {
                        sumStudent = Integer.valueOf(str[i]) + sumStudent;
                    }
                }
                str[i + 1] = String.valueOf(sumStudent);
                resultList.add(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    @Override
    public List<RWrongQuestion> queryRWrongQuestion() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        RWrongQuestion rWrongQuestion = new RWrongQuestion();
        rWrongQuestion.setCreator(userDetails.getUsername());
        List<RWrongQuestion> wrongQuestionList = reportDao.queryRWrongQuestion(rWrongQuestion);
        return wrongQuestionList;
    }

    @Override
    public List<RAboveSpecifiedMark> queryRAboveSpecifiedMark(String gradeno, String aboveMark, String dateArea) {
        HashMap markMap = reportDao.getRAboveSpecifiedMark(new RAboveSpecifiedMark());//查询升学模拟表
        List resultList = new ArrayList();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
//        reportDao.delRMarkArea(userDetails.getUsername());
        String[] cons = EConstants.markArea;
        ArrayUtils.add(cons, 0, "班级");

        EClassDTO eClassDTO = new EClassDTO();
        eClassDTO.setGradeno(gradeno);
        List<EClass> eClassList = eclassDao.queryEclass(eClassDTO);//获取指定学校、年级所对应班级列表
        RMarkArea rMarkArea = new RMarkArea();
        for (EClass eClass : eClassList) {

            rMarkArea.setCreator(userDetails.getUsername());
            rMarkArea.setClassno(eClass.getClassno());
            int sumStudent = 0;
            try {
                List<RMarkArea> rMarkAreaList = reportDao.queryRMarkArea(rMarkArea);//查询指定班级的成绩分布
                /**
                 * 生成classno+Mark+sum（MarkAreaNum）的组合数组String【】str，添加至List中
                 */
                String[] str = new String[cons.length + 3];
                str[0] = eClass.getClassname();
                int i = 0;
                for (RMarkArea area : rMarkAreaList) {
                    i++;
                    DecimalFormat df = new DecimalFormat("##,###,###");
                    str[i] = df.format(area.getMarkareanum()).toString();
                    if (i == 1) {
                        sumStudent = Integer.valueOf(str[1]);
                    } else {
                        sumStudent = Integer.valueOf(str[i]) + sumStudent;
                    }
                }
                str[i + 1] = String.valueOf(sumStudent);
                str[i + 2] = String.valueOf(markMap.get(eClass.getClassno()));
                resultList.add(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }
}
