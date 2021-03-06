package com.extjs.dao.impl;

import com.extjs.dao.EStudentMarkDao;
import com.extjs.model.EStudentMark;
import com.extjs.model.Page;
import org.hibernate.type.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import sun.plugin2.os.windows.FLASHWINFO;

import java.util.*;

/**
 * Created by jenny on 2017/5/12.
 */
@Repository
@Scope("prototype")

public class EStudentMarkDaoImpl implements EStudentMarkDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<EStudentMark> queryEStudentMark(EStudentMark eStudentMark, Page page) {
        List<EStudentMark> studentMarks = new ArrayList<EStudentMark>();
        String hql = this.getHql(eStudentMark);
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        if (null!=page&&null != page.getPageno() && page.getPageno() > 0 && null != page.getPagesize() && page.getPagesize() > 0) {
            query.setFirstResult((page.getPageno() - 1) * page.getPagesize());
            query.setMaxResults(page.getPagesize());
        }
        studentMarks = query.list();
        return studentMarks;
    }

    @Override
    public int getTotalCount(EStudentMark eStudentMark) {
        StringBuilder sb = new StringBuilder("select count(rowid) ");
        sb.append(this.getHql(eStudentMark));
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        int res = Integer.parseInt(query.uniqueResult().toString());
        return res;
    }

    @Override
    public List<EStudentMark> getAverageMark(EStudentMark studentMark, String gradeno) {
        StringBuilder sb = new StringBuilder("SELECT sum(MARK) / count(ROWID) AS aveMark, classno, subjectno FROM E_Student_Mark where testdate between to_date('");
        sb.append(studentMark.getBeginDate()).append("','yyyy-MM-dd') and to_date('").append(studentMark.getEndDate())
                .append("','yyyy-MM-dd') and classno in (")
                .append("select classno from e_class where gradeno='").append(gradeno).append("')")
                .append(" GROUP BY CLASSNO, SUBJECTNO ORDER BY SUBJECTNO,CLASSNO");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(sb.toString())
                .addScalar("aveMark", FloatType.INSTANCE)
                .addScalar("classno", StringType.INSTANCE)
//                .addScalar("tpno", StringType.INSTANCE)
                .addScalar("subjectno", StringType.INSTANCE);

        List list = query.list();

        List<EStudentMark> eStudentMarks = new ArrayList<>();

        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Object[] objects = (Object[]) iterator.next();
            studentMark = new EStudentMark();

            studentMark.setAveMark(Float.parseFloat(objects[0].toString()));
            studentMark.setClassno(objects[1].toString());
//            studentMark.setTpno(objects[2].toString());
            studentMark.setSubjectno(objects[2].toString());
            eStudentMarks.add(studentMark);
        }

//        List<EStudentMark> studentMarks = query.list();
        return eStudentMarks;
    }

    @Override
    public Float getAvgMarkByStudent(String studentID, String testDate, String subjectno) {
        StringBuilder stringBuilder = new StringBuilder("SELECT avg(mark) from EStudentMark  WHERE STUDENTNO='");
        stringBuilder.append(studentID).append("' and SUBJECTNO='").append(subjectno)
                .append("' and to_char(TESTDATE,'yyyyMM')='").append(testDate)
                .append("' and TPNO in (SELECT tpno from ETestpaper where examtype='2')");//考试类型=月考
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(stringBuilder.toString());
        Object object = query.uniqueResult();
        Float res = 0.0f;

        if (null != object) {
            res = Float.parseFloat(object.toString());
        } else {
        }
        return res;
    }

    @Override
    public int getMarkAreaNum(String tpno, String classno, String markArea) {
        String beginMark = markArea.substring(0, markArea.indexOf(","));
        String endMark = markArea.substring(markArea.indexOf(",") + 1, markArea.length());
        StringBuilder stringBuilder = new StringBuilder("select count(*) from EStudentMark where tpno='").append(tpno).append("' and classno='").append(classno)
                .append("' and mark between '").append(Float.parseFloat(beginMark)).append("' and '").append(Float.parseFloat(endMark)).append("'");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(stringBuilder.toString());
        int res = Integer.parseInt(query.uniqueResult().toString());
        return res;

    }

    @Override
    public LinkedHashMap<String, Float> getAvgMarkByClass(String classno, String year) {
        LinkedHashMap<String, Float> map = new LinkedHashMap<>();
        StringBuilder stringBuilder = new StringBuilder("SELECT avg(MARK) as avgMark,SUBJECTNO FROM E_Student_Mark WHERE CLASSNO = '")
                .append(classno).append("' AND to_char(testdate, 'yyyy') = '").append(year)
                .append("' AND tpno IN (SELECT tpno FROM E_Testpaper WHERE examtype IN ('2','4','5')) GROUP BY subjectno order by subjectno");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(stringBuilder.toString())
                .addScalar("avgMark", FloatType.INSTANCE)
                .addScalar("SUBJECTNO", StringType.INSTANCE);
        List list = query.list();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Object[] objects = (Object[]) iterator.next();
            map.put(objects[1].toString(), Float.parseFloat(objects[0].toString()));
        }
        return map;
    }

    @Override
    public LinkedHashMap<String, Float> getAvgMarkByGrade(String gradeno, String year) {
        LinkedHashMap<String, Float> map = new LinkedHashMap<>();
        StringBuilder stringBuilder = new StringBuilder("SELECT avg(MARK) as avgMark,SUBJECTNO FROM E_Student_Mark WHERE CLASSNO in (SELECT CLASSNO from E_CLASS where GRADENO='")
                .append(gradeno).append("') AND to_char(testdate, 'yyyy') = '").append(year)
                .append("' AND tpno IN (SELECT tpno FROM E_Testpaper WHERE examtype IN ('2','4','5')) GROUP BY subjectno  order by subjectno");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(stringBuilder.toString())
                .addScalar("avgMark", FloatType.INSTANCE)
                .addScalar("SUBJECTNO", StringType.INSTANCE);
        List list = query.list();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Object[] objects = (Object[]) iterator.next();
            map.put(objects[1].toString(), Float.parseFloat(objects[0].toString()));
        }
        return map;
    }

    @Override
    public Float getAvgMark(String classno, String testDate, String subjectno) {
        StringBuilder stringBuilder = new StringBuilder("SELECT avg(mark) from EStudentMark WHERE CLASSNO='" + classno + "'")
                .append(" and to_char(TESTDATE,'yyyyMM')='" + testDate + "' and SUBJECTNO='" + subjectno + "'")
                .append(" and TPNO in (SELECT tpno from ETestpaper where examtype='2')");//考试类型=月考
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(stringBuilder.toString());
        Float res = 0.0f;

        if (null != query.uniqueResult()) {
            res = Float.parseFloat(query.uniqueResult().toString());
        } else {
        }
        return res;
    }

    @Override
    public Float getAvgMiddleOrFinal(String classno, String year, String subjectno, String examType) {
        StringBuilder stringBuilder = new StringBuilder("SELECT avg(mark) from EStudentMark WHERE CLASSNO='" + classno + "'")
                .append(" and to_char(TESTDATE,'yyyy')='" + year + "' and SUBJECTNO='" + subjectno + "'")
                .append(" and TPNO in (SELECT tpno from ETestpaper where examtype='" + examType).append("')");//考试类型=4期中或5期末
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(stringBuilder.toString());
        Float res = 0.0f;

        if (null != query.uniqueResult()) {
            res = Float.parseFloat(query.uniqueResult().toString());
        } else {
        }
        return res;
    }

    @Override
    public Float getAvegMiddleOrFinalByStudent(String studentID, String year, String subjectno, String examType) {
        StringBuilder stringBuilder = new StringBuilder("SELECT avg(mark) from EStudentMark WHERE studentno='" + studentID + "'")
                .append(" and to_char(TESTDATE,'yyyy')='" + year + "' and SUBJECTNO='" + subjectno + "'")
                .append(" and TPNO in (SELECT tpno from ETestpaper where examtype='" + examType).append("')");//考试类型=4期中或5期末
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(stringBuilder.toString());
        Float res = 0.0f;

        if (null != query.uniqueResult()) {
            res = Float.parseFloat(query.uniqueResult().toString());
        } else {
        }
        return res;
    }

    @Override
    public HashMap<String, Integer> getStudentNum() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        String sql = "select count(ROWID) as rownums,tpno from E_STUDENT_MARK t GROUP BY t.TPNO";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(sql)
                .addScalar("rownums", IntegerType.INSTANCE)
                .addScalar("tpno", StringType.INSTANCE);
        List list = query.list();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Object[] objects = (Object[]) iterator.next();
            hashMap.put(objects[1].toString(), Integer.parseInt(objects[0].toString()));
        }
        return hashMap;
    }

    @Override
    public int getMarkAreaTotalNum(String classno, String markArea, String tpnoString) {
        String beginMark = markArea.substring(0, markArea.indexOf(","));
        String endMark = markArea.substring(markArea.indexOf(",") + 1, markArea.length());
        StringBuilder stringBuilder = new StringBuilder(
                "SELECT * from (SELECT sum(a.mark) as totalMark,a.classno,a.studentno FROM (SELECT * FROM E_STUDENT_MARK t WHERE  t.CLASSNO = '");
        stringBuilder.append(classno).append("' and tpno in (").append(tpnoString)
                .append(")) a GROUP BY a.classno,a.STUDENTNO) where totalMark BETWEEN ")
                .append(Float.parseFloat(beginMark)).append(" and ").append(Float.parseFloat(endMark));
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(stringBuilder.toString());
        List list = query.list();
        return list.size();
    }

    private String getHql(EStudentMark eStudentMark) {
        StringBuilder sb = new StringBuilder("from EStudentMark where 1=1");
        if (null != eStudentMark.getCreator() && !"".equals(eStudentMark.getCreator())) {
            sb.append(" and creator='").append(eStudentMark.getCreator()).append("'");

        }
        if (null != eStudentMark.getStudentname() && !"".equals(eStudentMark.getStudentname())) {
            sb.append(" and studentname='" + eStudentMark.getStudentname() + "'");
        }
        if (null != eStudentMark.getStudentno() && eStudentMark.getStudentno().length() > 0) {
            sb.append(" and studentno='" + eStudentMark.getStudentno() + "'");
        }
        if (null != eStudentMark.getSubjectno() && !"".equals(eStudentMark.getSubjectno()) && !"noselected".equals(eStudentMark.getSubjectno())) {
            sb.append(" and subjectno='" + eStudentMark.getSubjectno() + "'");
        }
        if (null != eStudentMark.getTpname() && !"".equals(eStudentMark.getTpname())) {
            sb.append(" and tpname like '%" + eStudentMark.getTpname() + "%'");
        }
        if (null != eStudentMark.getTpno() && eStudentMark.getTpno().length() > 0) {
            sb.append(" and tpno='" + eStudentMark.getTpno() + "'");
        }
        if (null != eStudentMark.getClassno() && eStudentMark.getClassno().length() > 0) {
            sb.append(" and classno='" + eStudentMark.getClassno() + "'");
        }
        sb.append(" order by tpno,mark desc,createdate desc");
        return sb.toString();
    }

    @Override
    public void saveOrUpdateEStudentMark(EStudentMark eStudentMark) {
        Session session = sessionFactory.getCurrentSession();
        if (null != eStudentMark) {
//            session.saveOrUpdate(eStudentMark);
            session.merge(eStudentMark);
            session.flush();
        }
    }

    @Override
    public void modifOnlyMark(EStudentMark studentMark) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("update EStudentMark set markone=:markone,marktwo=:marktwo,mark=:mark where id=:id");
        query.setFloat("markone", studentMark.getMarkone());
        query.setFloat("marktwo", studentMark.getMarktwo());
        query.setFloat("mark", studentMark.getMarkone() + studentMark.getMarktwo());
        query.setString("id", studentMark.getId());
        query.executeUpdate();
        session.flush();
    }

    @Override
    public void delEStudentMark(EStudentMark eStudentMark) {
        Session session = sessionFactory.getCurrentSession();
        if (null != eStudentMark) {
            session.delete(eStudentMark);
            session.flush();
        }
    }

    @Override
    public void delEStudentMarkByCreator(String creator) {
        Session session = sessionFactory.getCurrentSession();

        if (null != creator && !"".equals(creator)) {
            String sql = "delete EStudentMark where creator=:creator";
            session.createQuery(sql).setString("creator", creator).executeUpdate();
            session.flush();
        }

    }
}
