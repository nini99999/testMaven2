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
import java.util.LinkedHashMap;
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
    @Autowired
    private EclassService eclassService;
    @Autowired
    private EstudentService estudentService;

    @Autowired
    private EschoolService eschoolService;
    @Autowired
    private EsubjectsService esubjectsService;
    @Autowired
    private EstudentMarkService estudentMarkService;
    @Autowired
    private EtestpaperService etestpaperService;
    @Autowired
    private EteacherClassService eteacherClassService;
    @Autowired
    private EwrongStudentService ewrongStudentService;

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
        DecimalFormat df = new DecimalFormat("##,###,###.##");
        for (RClassMark classMark : rClassMarkList) {
            resultMap.put(classMark.getClassno() + classMark.getSubjectno(), String.valueOf(df.format(classMark.getMark())));
        }


        return resultMap;
    }

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
            DecimalFormat df = new DecimalFormat("##,###,###.##");
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
    public List<String[]> queryRYearMark(String year, String gradeno, String subjectno, String studentno, String studentname) {
        List<String[]> resultList = new ArrayList<String[]>();

        EClassDTO eClassDTO = new EClassDTO();
        try {
            DecimalFormat df = new DecimalFormat("##,###,###.##");
            String testDate = "";
            Float aveMark = 0.0f;//月考平均分
            Float middleMark = 0.0f;//期中平均分
            Float finalMark = 0.0f;//期末平均分
            Float yearAvgMark;//全年平均分
            eClassDTO.setSchoolno(eschoolService.getSchoolnoByContext());
            eClassDTO.setGradeno(gradeno);
            List<EClassDTO> eClassDTOList = eclassService.queryEclassByDTO(eClassDTO);//获取指定学校、年级所对应班级列表

            for (EClassDTO classDTO : eClassDTOList) {//循环班级
                yearAvgMark = 0.0f;
                //根据班级、指定学科查找的授课教师
                VTeacherClass vTeacherClass = new VTeacherClass();
                vTeacherClass.setClassno(classDTO.getClassno());
                vTeacherClass.setSubjectno(subjectno);
                VTeacherClass teacherClass = eteacherClassService.getTeacherClass(vTeacherClass);
                //按查询表头定义该格式的数据
                String[] strings = new String[18];
                strings[0] = teacherClass.getClassno();
                strings[1] = teacherClass.getTeachername();
                int aboveZero = 0;
                for (int i = 1; i <= 12; i++) {//12个月循环，获取每个月、月考的平均成绩
                    if (i < 10) {
                        testDate = year + "0" + String.valueOf(i);
                    } else {
                        testDate = year + String.valueOf(i);
                    }
                    aveMark = estudentMarkService.getAvgMark(classDTO.getClassno(), testDate, subjectno);
                    if (aveMark > 0) {
                        aboveZero += 1;
                        yearAvgMark += aveMark;
                    }

                    strings[i + 1] = df.format(aveMark).toString();

                }
                middleMark = estudentMarkService.getAvgMiddleOrFinal(classDTO.getClassno(), year, subjectno, "4");//期中平均分
                finalMark = estudentMarkService.getAvgMiddleOrFinal(classDTO.getClassno(), year, subjectno, "5");//期末平均分
                if (middleMark > 0) {
                    aboveZero += 1;
                    yearAvgMark += middleMark;
                }
                if (finalMark > 0) {
                    aboveZero += 1;
                    yearAvgMark += finalMark;
                }


                strings[14] = df.format(middleMark).toString();
                strings[15] = df.format(finalMark).toString();
                if (aboveZero > 0) {
                    strings[16] = df.format(yearAvgMark / aboveZero).toString();
                } else {
                    strings[16] = String.valueOf(0);
                }

                resultList.add(strings);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public List<RYearMarkStudent> queryRYearMarkStudent(String year, String studentID, String subjectno, String studentno) {
        List<RYearMarkStudent> rYearMarkStudents = new ArrayList<RYearMarkStudent>();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        RYearMarkStudent rYearMarkStudent = new RYearMarkStudent();
        rYearMarkStudent.setCreator(userDetails.getUsername());
        rYearMarkStudent.setStudentname(studentID);
        rYearMarkStudent.setSubjectno(subjectno);
        rYearMarkStudent.setStudentno(studentno);
        rYearMarkStudents = reportDao.queryRYearMarkStudent(rYearMarkStudent);
        return rYearMarkStudents;
    }

    @Override
    public List<String[]> getYearMarkStudent(String year, String studentID) {
        List<String[]> resultList = new ArrayList<String[]>();
        DecimalFormat df = new DecimalFormat("##,###,###.##");
        Float aveMark = 0.0f;//月考平均分
        Float middleMark = 0.0f;//期中平均分
        Float finalMark = 0.0f;//期末平均分
        Float yearAvgMark;//全年平均分

        String testDate = "";
//        EsubjectsDTO esubjectsDTO = new EsubjectsDTO();
        List<EsubjectsDTO> esubjectsDTOS = esubjectsService.queryEsubjectsList();
        for (EsubjectsDTO esubjectsDTO : esubjectsDTOS) {//循环学科
            yearAvgMark = 0.0f;
            //按查询表头定义该格式的数据
            String[] strings = new String[16];
            strings[0] = esubjectsDTO.getSubjectname();
            int aboveZero = 0;
            for (int i = 1; i <= 12; i++) {//12个月循环，获取每个月、月考的平均成绩(指定学生、指定学科、指定月份)
                if (i < 10) {
                    testDate = year + "0" + String.valueOf(i);
                } else {
                    testDate = year + String.valueOf(i);
                }
                aveMark = estudentMarkService.getAvgMarkByStudent(studentID, testDate, esubjectsDTO.getSubjectno());
                if (aveMark > 0) {
                    aboveZero += 1;
                    yearAvgMark += aveMark;
                }
                strings[i] = df.format(aveMark).toString();
            }
            middleMark = estudentMarkService.getAvgMiddleOrFinalByStudent(studentID, year, esubjectsDTO.getSubjectno(), "4");//期中平均分
            finalMark = estudentMarkService.getAvgMiddleOrFinalByStudent(studentID, year, esubjectsDTO.getSubjectno(), "5");//期末平均分
            if (middleMark > 0) {
                aboveZero += 1;
                yearAvgMark += middleMark;
            }
            if (finalMark > 0) {
                aboveZero += 1;
                yearAvgMark += finalMark;
            }
            strings[13] = df.format(middleMark).toString();
            strings[14] = df.format(finalMark).toString();
            if (aboveZero > 0) {
                strings[15] = df.format(yearAvgMark / aboveZero).toString();
            } else {
                strings[15] = String.valueOf(0);
            }
            resultList.add(strings);

        }
        return resultList;
    }

    @Override
    public Float[] getAvgMarkByClass(String studentID, String year) {
        EStudentDTO studentDTO = estudentService.getStudentByID(studentID);
        LinkedHashMap<String, Float> map = estudentMarkService.getAvgMarkByClass(studentDTO.getClassno(), year);

        List<EsubjectsDTO> esubjectsDTOS = esubjectsService.queryEsubjectsList();
        Float[] floats = new Float[esubjectsDTOS.size()];
        int i = 0;
        for (EsubjectsDTO esubjectsDTO : esubjectsDTOS) {
            floats[i] = map.get(esubjectsDTO.getSubjectno());
            i++;
        }

        return floats;
    }

    @Override
    public Float[] getAvgMarkByGrade(String studentID, String year) {
        EStudentDTO studentDTO = estudentService.getStudentByID(studentID);
        LinkedHashMap<String, Float> map = estudentMarkService.getAvgMarkByGrade(studentDTO.getGradeno(), year);

        List<EsubjectsDTO> esubjectsDTOS = esubjectsService.queryEsubjectsList();
        Float[] floats = new Float[esubjectsDTOS.size()];
        int i = 0;
        for (EsubjectsDTO esubjectsDTO : esubjectsDTOS) {
            floats[i] = map.get(esubjectsDTO.getSubjectno());
            i++;
        }
        return floats;
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
                this.autoAddMarkAreaTotal(gradeno, tpno);
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
    public List<RWrongQuestion> queryRWrongQuestion(String beginDate, String endDate, String subjectno, String gradeno, String classno) {
        List<RWrongQuestion> wrongQuestions = ewrongStudentService.getWrongsByDateArea(beginDate, endDate, subjectno, gradeno);
        List<RWrongQuestion> result = new ArrayList<>();
        HashMap<String, Integer> map = estudentMarkService.getStudentNum();
        for (RWrongQuestion wrongQuestion : wrongQuestions) {
            wrongQuestion.setTestnums(map.get(wrongQuestion.getTpno()));
            result.add(wrongQuestion);
        }
        return result;
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getPrincipal();
//        RWrongQuestion rWrongQuestion = new RWrongQuestion();
//        rWrongQuestion.setCreator(userDetails.getUsername());
//        List<RWrongQuestion> wrongQuestionList = reportDao.queryRWrongQuestion(rWrongQuestion);
//        return wrongQuestionList;
    }

    @Override
    public List<RAboveSpecifiedMark> queryRAboveSpecifiedMark(String gradeno, String aboveMark, String tpnoString) {
//        HashMap markMap = reportDao.getRAboveSpecifiedMark(new RAboveSpecifiedMark());//查询升学模拟表
        this.queryRMarkArea(gradeno, tpnoString, null);//生成数据至成绩分布表
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
        String markArea = aboveMark + "," + String.valueOf(EConstants.maxMark);
        for (EClassDTO classDTO : eClassDTOList) {
            //查指定区间内（aboveMark,MaxMark）、指定班级、指定试卷列表的学生数量
            Integer above = estudentMarkService.getMareAreaTotalNum(classDTO.getClassno(), markArea, tpnoString);
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
                str[i + 2] = String.valueOf(above);
                resultList.add(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }
}
