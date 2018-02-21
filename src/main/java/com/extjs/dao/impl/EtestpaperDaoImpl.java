package com.extjs.dao.impl;

import com.extjs.dao.EtestpaperDao;
import com.extjs.model.ETestpaper;
import com.extjs.model.ETestpaperDTO;
import com.extjs.model.Page;
import com.extjs.util.ReflectionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jenny on 2017/4/4.
 */
@Repository
@Scope("prototype")
public class EtestpaperDaoImpl implements EtestpaperDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    /**
     * if creator="All",不根据创建人进行查询
     */
    public List<ETestpaper> queryEtestPaper(ETestpaperDTO eTestpaperDTO, Page page) {
        String hql = this.getHql(eTestpaperDTO) + " order by schoolno,gradeno,term,subjectno,tpno";
        List<ETestpaper> eTestpaperList = null;
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = session.createQuery(hql);
            if (null != page && null != page.getPageno() && page.getPageno() > 0 && null != page.getPagesize() && page.getPagesize() > 0) {
                query.setFirstResult((page.getPageno() - 1) * page.getPagesize());
                query.setMaxResults(page.getPagesize());
            }
            eTestpaperList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eTestpaperList;
    }

    private String getHql(ETestpaperDTO eTestpaperDTO) {
        StringBuilder sb = new StringBuilder();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        sb.append("from ETestpaper where 1=1");
        if (!"All".equals(eTestpaperDTO.getCreator())) {
            sb.append(" and creator='" + userDetails.getUsername() + "'");
        }
        if (null != eTestpaperDTO.getSchoolno() && !"".equals(eTestpaperDTO.getSchoolno())) {
            sb.append(" and schoolno='" + eTestpaperDTO.getSchoolno() + "'");
        }
        if (null != eTestpaperDTO.getGradeno() && !"".equals(eTestpaperDTO.getGradeno())) {
            sb.append(" and gradeno='" + eTestpaperDTO.getGradeno() + "'");
        }
        if (null != eTestpaperDTO.getSubjectno() && !"".equals(eTestpaperDTO.getSubjectno())) {
            sb.append(" and subjectno='" + eTestpaperDTO.getSubjectno() + "'");
        }
        if (null != eTestpaperDTO.getTerm() && !"".equals(eTestpaperDTO.getTerm())) {
            sb.append(" and term='" + eTestpaperDTO.getTerm() + "'");
        }
        if (null != eTestpaperDTO.getExamtype() && !"".equals(eTestpaperDTO.getExamtype())) {
            sb.append(" and examtype='" + eTestpaperDTO.getExamtype() + "'");
        }
        if (null != eTestpaperDTO.getTpno() && !"".equals(eTestpaperDTO.getTpno())) {
            sb.append(" and tpno='" + eTestpaperDTO.getTpno() + "'");
        }
        if (null != eTestpaperDTO.getTpname() && !"".equals(eTestpaperDTO.getTpname())) {
            sb.append(" and tpname like '%" + eTestpaperDTO.getTpname() + "%'");
        }
        if (null != eTestpaperDTO.getStartDate() && eTestpaperDTO.getStartDate().length() > 0 && null != eTestpaperDTO.getEndDate() && eTestpaperDTO.getEndDate().length() > 0) {
            sb.append(" and testdate BETWEEN to_date('" + eTestpaperDTO.getStartDate() + "', 'yyyy-mm-dd') AND to_date('" + eTestpaperDTO.getEndDate() + "', 'yyyy-mm-dd')");
        }
//        sb.append(" order by schoolno,gradeno,term,subjectno,tpno");
        return sb.toString();
    }

    @Override
    public ETestpaper getTestPaper(String tpno, String tpname) {
        ETestpaper result = new ETestpaper();
        ETestpaperDTO eTestpaperDTO = new ETestpaperDTO();
        if (null != tpno && tpno.length() > 0) {
            eTestpaperDTO.setTpno(tpno);
        } else {
            if (null != tpname && tpname.length() > 0) {
                eTestpaperDTO.setTpname(tpname);
            }
        }
        eTestpaperDTO.setCreator("All");
        List<ETestpaper> eTestpaperList = this.queryEtestPaper(eTestpaperDTO, new Page());

        for (ETestpaper eTestpaper : eTestpaperList) {
            result = eTestpaper;
        }
        return result;
    }

    @Override
    public Integer getSumQuestionNum(String tpno) {
        String hql = "select sum(questionnum) from EPaperQType where tpno=:str";
        Session session = sessionFactory.getCurrentSession();
        Object object = session.createQuery(hql).setString("str", tpno).uniqueResult();
        int i = 0;
        if (null != object) {
            i = Integer.parseInt(String.valueOf(object));
        }
        return i;
    }

    @Override
    public void modifEtestPaper(ETestpaperDTO eTestpaperDTO) {
        Session session = sessionFactory.getCurrentSession();
        ETestpaper eTestpaper = new ETestpaper();
        ReflectionUtil.copyProperties(eTestpaperDTO, eTestpaper);
        session.saveOrUpdate(eTestpaper);
        session.flush();
    }

    @Override
    public void delEtestPaper(ETestpaperDTO eTestpaperDTO) {
        Session session = sessionFactory.getCurrentSession();
        ETestpaper eTestpaper = new ETestpaper();
        ReflectionUtil.copyProperties(eTestpaperDTO, eTestpaper);
        session.delete(eTestpaper);
        session.flush();
    }

    @Override
    public int getTotalCount(ETestpaperDTO eTestpaperDTO) {
        String hql = "select count(*) " + this.getHql(eTestpaperDTO);
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        int res = Integer.parseInt(query.uniqueResult().toString());
        return res;
    }
}
