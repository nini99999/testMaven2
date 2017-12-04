package com.extjs.service.impl;


import com.extjs.dao.ReportDao;
import com.extjs.model.*;
import com.extjs.service.*;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jenny on 2017/4/9.
 */
@Service
@Scope("prototype")
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportDao reportDao;
    //    @Autowired
//    private EclassDao eclassDao;
    @Autowired
    private EclassService eclassService;
    @Autowired
//    private EstudentDao estudentDao;
    private EstudentService estudentService;

    @Autowired
    private EschoolService eschoolService;
    @Autowired
    private EsubjectsService esubjectsService;
    @Autowired
    private EstudentMarkService estudentMarkService;
    @Autowired
    private EtestpaperService etestpaperService;

    @Override
    public List<RClassMark> queryRClassMark(RClassMark rClassMark) {
        List<RClassMark> rClassMarkList = reportDao.queryRClassMark(rClassMark);
        return rClassMarkList;
    }

    private HashMap<String, String> getRClassMark(String gradeno) {
        HashMap<String, String> resultMap = new HashMap<>();
        RClassMark rClassMark = new RClassMark();
        rClassMark.setGradeno(gradeno);
        rClassMark.setCreator(this.getUserName());
        List<RClassMark> rClassMarkList = reportDao.queryRClassMark(rClassMark);
        for (RClassMark classMark : rClassMarkList) {
            resultMap.put(classMark.getClassno() + classMark.getSubjectno(), String.valueOf(classMark.getMark()));
        }


        return resultMap;
    }

    //    public UserDTO getCurrentUser() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getPrincipal();
//        UserDTO userDTO = new UserDTO();
//        try {
//            userDTO = userService.getUserByUnique(userDetails.getUsername());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return userDTO;
//    }
//
    public String getUserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
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
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public List reckonClassMark(RClassMark rClassMark) {
        List<EsubjectsDTO> esubjectsDTOS = esubjectsService.queryEsubjectsList();//获取学科列表
        List resultList = new ArrayList();
//        RClassMark rClassMark;
        EClassDTO eClassDTO = new EClassDTO();
        try {
            this.delRClassMark();//删除当前用户的报表数据
            this.autoAddClassMark(rClassMark);//生成数据至班级平均成绩表

            eClassDTO.setSchoolno(eschoolService.getSchoolnoByContext());
            eClassDTO.setGradeno(rClassMark.getGradeno());
//            List<EClass> eClassList = eclassDao.queryEclass(eClassDTO);//获取指定学校、年级所对应班级列表
            List<EClassDTO> eClassDTOList = eclassService.queryEclassByDTO(eClassDTO);
            HashMap<String, String> aveMarkMap = this.getRClassMark(rClassMark.getGradeno());//获取平均成绩map
            for (EClassDTO classDTO : eClassDTOList) {
                rClassMark = new RClassMark();
                rClassMark.setClassno(classDTO.getClassno());
                List<RClassMark> rClassMarkList = this.queryRClassMark(rClassMark);//查询指定班级对应的成绩数据
/**
 * 生成classno+Mark+sum（Mark）的组合数组String【】str，添加至List中
 */
                String[] str = new String[esubjectsDTOS.size() + 2];
//                String[] str = new String[rClassMarkList.size() + 2];
                int i = 0;
                float sum = 0.0f;
                str[0] = classDTO.getClassname();
//            str[0] = rClassMark.getClassno();
                //循环学科列表，新城str[]
                for (EsubjectsDTO esubjectsDTO : esubjectsDTOS) {
                    i++;
                    str[i] = aveMarkMap.get(classDTO.getClassno() + esubjectsDTO.getSubjectno());//根据年级和学科获取平均成绩（唯一性约束：班级+学科+creator）
                    if (null == str[i]) {
                        str[i] = String.valueOf(0);
                    }
                    sum = Float.parseFloat(str[i]) + sum;

                }
                DecimalFormat df = new DecimalFormat("##,###,###.##");
                String sdf = df.format(sum).toString();
                str[i + 1] = sdf;
                resultList.add(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * 获取指定条件的平均成绩列表，循环插入数据库中
     *
     * @param classMark
     * @throws SysException
     */
    @Override
    public void autoAddClassMark(RClassMark classMark) throws SysException {
        HashMap<String, EClassDTO> classMap = eclassService.queryEclassMap();//获取所有班级集合
        HashMap<String, ETestpaperDTO> testPaperMap = etestpaperService.getEtestPaper();//获取所有试卷信息
        EClassDTO classDTO;
        EStudentMark studentMark = new EStudentMark();
        studentMark.setBeginDate(classMark.getBeginDate());
        studentMark.setEndDate(classMark.getEndDate());
        List<EStudentMark> studentMarks = estudentMarkService.getAverageMark(studentMark, classMark.getGradeno());//获取指定条件的平均成绩列表
        for (EStudentMark eStudentMark : studentMarks) {
            classMark = new RClassMark();
            classDTO = classMap.get(eStudentMark.getClassno());
            classMark.setClassno(eStudentMark.getClassno());
            classMark.setGradeno(classDTO.getGradeno());
            classMark.setSchoolno(classDTO.getSchoolno());
            classMark.setMark(eStudentMark.getAveMark());
//            classMark.setTpno(eStudentMark.getTpno());
            classMark.setSubjectno(eStudentMark.getSubjectno());
//            classMark.setTestdate(testPaperMap.get(eStudentMark.getTpno()).getTestdate());
            //...add to RClassMark
            this.addClassMark(classMark);
        }

    }

    @Override
    public void addClassMark(RClassMark classMark) throws SysException {

        classMark.setCreator(this.getUserName());
        classMark.setCreatedate(new java.sql.Date(System.currentTimeMillis()));
        reportDao.addRClassMark(classMark);
    }

    @Override
    public void delRClassMark() throws SysException {
        reportDao.delRClassMark(this.getUserName());
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
            EStudentDTO eStudentDTO = new EStudentDTO();
            eStudentDTO = estudentService.getStudentByID(studentno);
//            eStudent = estudentDao.getEstudentByCountryID(studentno);
            rYearMark.setClassno(eStudentDTO.getClassno());
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
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public List<RMarkArea> queryRMarkArea(String gradeno, String tpno, String subjectno) {
        List resultList = new ArrayList();
        String userName = this.getUserName();
        try {
            this.delMarkArea(userName);//delete 当前用户记录

            String[] cons;
            if (null != subjectno && !"".equals(subjectno)) {//判断是取总成绩还是取单科成绩：空-总成绩；非空-学科成绩

                cons = EConstants.subjectMarkArea;
                this.autoAddMarkAreaBySubject(tpno, gradeno);//自动生成数据至成绩区间分布表（按学科）
            } else {
                cons = EConstants.markArea;
                this.autoAddMarkAreaTotal(gradeno,tpno);
            }

            ArrayUtils.add(cons, 0, "班级");

//        resultList.add(cons);
            EClassDTO eClassDTO = new EClassDTO();
            eClassDTO.setGradeno(gradeno);
            List<EClassDTO> eClassDTOList = eclassService.queryEclassByDTO(eClassDTO);//获取指定学校、年级所对应班级列表
            RMarkArea rMarkArea = new RMarkArea();
            for (EClassDTO classDTO : eClassDTOList) {
                rMarkArea.setCreator(userName);
                rMarkArea.setClassno(classDTO.getClassno());
//            rMarkArea.setSubjectno(subjectno);
                int sumStudent = 0;
                try {
                    List<RMarkArea> rMarkAreaList = reportDao.queryRMarkArea(rMarkArea);//查询指定班级的成绩分布
                    /**
                     * 生成classno+Mark+sum（MarkAreaNum）的组合数组String【】str，添加至List中
                     */
                    String[] str = new String[cons.length + 2];
                    str[0] = classDTO.getClassname();
                    int i = 0;
                    for (RMarkArea area : rMarkAreaList) {
                        i++;
                        DecimalFormat df = new DecimalFormat("##,###,###");
                        str[i] = df.format(area.getMarkareanum()).toString();
                        sumStudent = Integer.valueOf(str[i]) + sumStudent;
                    }
                    str[i + 1] = String.valueOf(sumStudent);
                    resultList.add(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public void delMarkArea(String creator) throws SysException {
        reportDao.delRMarkArea(this.getUserName());
    }

    @Override
    public void addMarkArea(RMarkArea markArea) throws SysException {
        markArea.setCreator(this.getUserName());
        markArea.setCreatedate(new java.sql.Date(System.currentTimeMillis()));
        reportDao.addRMarkArea(markArea);
    }

    @Override
    public void autoAddMarkAreaBySubject(String tpno, String gradeno) throws SysException {
        String[] strings = EConstants.subjectMarkArea;
        EClassDTO classDTO = new EClassDTO();
        classDTO.setGradeno(gradeno);
        List<EClassDTO> classDTOList = eclassService.queryEclassByDTO(classDTO);//根据年级查询班级列表
        for (EClassDTO eClassDTO : classDTOList) {//按班级循环，逐条插入成绩区间表
            for (int i = 0; i < strings.length; i++) {//按成绩区间循环，插入指定班级、试卷和成绩区间的数据
                int studentCount = estudentMarkService.getMarkAreaNum(tpno, eClassDTO.getClassno(), strings[i]);//获取指定试卷、班级的成绩区间分布数

                RMarkArea rMarkArea = new RMarkArea();
                rMarkArea.setClassno(eClassDTO.getClassno());
                rMarkArea.setMarkarea(strings[i]);
                rMarkArea.setMarkareanum(studentCount);
                this.addMarkArea(rMarkArea);
            }
        }
    }

    @Override
    public void autoAddMarkAreaTotal(String gradeno, String tpnoString) throws SysException {
        String[] strings = EConstants.markArea;
        EClassDTO classDTO = new EClassDTO();
        classDTO.setGradeno(gradeno);
        List<EClassDTO> classDTOList = eclassService.queryEclassByDTO(classDTO);//根据年级查询班级列表
        for (EClassDTO eClassDTO : classDTOList) {//按班级循环，逐条插入成绩区间表
            for (int i = 0; i < strings.length; i++) {//按成绩区间循环，插入指定班级、试卷和成绩区间的数据
                int studentCount = estudentMarkService.getMareAreaTotalNum(eClassDTO.getClassno(), strings[i], tpnoString);
                RMarkArea rMarkArea = new RMarkArea();
                rMarkArea.setClassno(eClassDTO.getClassno());
                rMarkArea.setMarkarea(strings[i]);
                rMarkArea.setMarkareanum(studentCount);
                this.addMarkArea(rMarkArea);
            }
        }
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
        List<EClassDTO> eClassDTOList = eclassService.queryEclassByDTO(eClassDTO);//获取指定学校、年级所对应班级列表
        RMarkArea rMarkArea = new RMarkArea();
        for (EClassDTO classDTO : eClassDTOList) {

            rMarkArea.setCreator(userDetails.getUsername());
            rMarkArea.setClassno(classDTO.getClassno());
            int sumStudent = 0;
            try {
                List<RMarkArea> rMarkAreaList = reportDao.queryRMarkArea(rMarkArea);//查询指定班级的成绩分布
                /**
                 * 生成classno+Mark+sum（MarkAreaNum）的组合数组String【】str，添加至List中
                 */
                String[] str = new String[cons.length + 3];
                str[0] = classDTO.getClassname();
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
                str[i + 2] = String.valueOf(markMap.get(classDTO.getClassno()));
                resultList.add(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }
}
