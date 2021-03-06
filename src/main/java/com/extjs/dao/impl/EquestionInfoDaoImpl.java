package com.extjs.dao.impl;

import com.extjs.dao.EquestionInfoDao;
import com.extjs.model.EQuestionInfo;
import com.extjs.model.Page;
import com.extjs.model.VQuestionandinfo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jenny on 2017/6/22.
 */
@Repository
@Scope("prototype")
public class EquestionInfoDaoImpl implements EquestionInfoDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<EQuestionInfo> getQuestionInfo(EQuestionInfo eQuestionInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("from EQuestionInfo where 1=1");
        if (null != eQuestionInfo.getId() && !"".equals(eQuestionInfo.getId())) {
            sb.append(" and id='" + eQuestionInfo.getId() + "'");
        } else {
            if (null != eQuestionInfo.getQuestionid() && !"".equals(eQuestionInfo.getQuestionid())) {
                sb.append(" and questionid='" + eQuestionInfo.getQuestionid() + "'");
            }
            if (null != eQuestionInfo.getQuestiontype() && !"".equals(eQuestionInfo.getQuestiontype())) {
                sb.append(" and questiontype='" + eQuestionInfo.getQuestiontype() + "'");
            }
            if (null != eQuestionInfo.getCreator() && !"".equals(eQuestionInfo.getCreator())) {
                sb.append(" and creator='" + eQuestionInfo.getCreator() + "'");
            }
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sb.toString());
        List<EQuestionInfo> questionInfoList = query.list();
        return questionInfoList;
    }

    @Override
    public void saveOrUpdateQuestionInfo(EQuestionInfo eQuestionInfo) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(eQuestionInfo);
        session.flush();
    }

    @Override
    public void delQuestionInfo(String questionid) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete from EQuestionInfo where questionid='" + questionid + "'");
        query.executeUpdate();
        session.flush();
    }

    @Override
    public List<VQuestionandinfo> getQuestionAndInfos(VQuestionandinfo questionandinfo, Page page) {
//        StringBuilder stringBuilder = new StringBuilder("select * " + this.getHql(questionandinfo));
//        if (null != page.getPageno() && page.getPageno() > 0 && null != page.getPagesize() && page.getPagesize() > 0) {
//            int beginRow = (page.getPageno() - 1) * page.getPagesize() + 1;
//            int endRow = page.getPagesize() + beginRow - 1;
//            stringBuilder.append(" and rownum between " + beginRow + " and " + endRow + " order by questionid,questionno");
////            query.setFirstResult((page.getPageno() - 1) * page.getPagesize());
////            query.setMaxResults(page.getPagesize());
//        }
        List<String> list = this.getQuestionIDS(questionandinfo, page);
        StringBuilder stringBuilder = new StringBuilder("select * from V_Questionandinfo where questionid in(:questionIDS) order by questionid,questionno");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(stringBuilder.toString()).addEntity(VQuestionandinfo.class).setParameterList("questionIDS", list);

        List<VQuestionandinfo> res = query.list();
        return res;
    }

    /**
     * 根据查询条件查询出指定页数的questionID，并拼接成字符串返回
     *
     * @param questionandinfo
     * @param page
     * @return
     */
    private List<String> getQuestionIDS(VQuestionandinfo questionandinfo, Page page) {
//        SELECT QUESTIONID from (SELECT ROWNUM as rn,t.* FROM (SELECT DISTINCT V_Questionandinfo.questionid FROM V_QUESTIONANDINFO) t) WHERE rn BETWEEN 2 AND 3
        StringBuilder stringBuilder = new StringBuilder("SELECT QUESTIONID from (SELECT ROWNUM as rn,t.* FROM (SELECT DISTINCT V_Questionandinfo.questionid ");
        stringBuilder.append(this.getHql(questionandinfo)).append(") t)");

        if (null!=page&&null != page.getPageno() && page.getPageno() > 0 && null != page.getPagesize() && page.getPagesize() > 0) {
            int beginRow = (page.getPageno() - 1) * page.getPagesize() + 1;
            int endRow = page.getPagesize() + beginRow - 1;
            stringBuilder.append(" where rn between " + beginRow + " and " + endRow);
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(stringBuilder.toString()).addScalar("QUESTIONID", StringType.INSTANCE);
        List<String> list = query.list();
//        StringBuilder sb = new StringBuilder("");
//        for (String string : list) {
//            sb.append(string).append(",");
//        }
//        return sb.toString().substring(0, sb.toString().length() - 1);
        return list;
    }

    private String getHql(VQuestionandinfo questionandinfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("from V_Questionandinfo  where 1=1");
        if (null != questionandinfo.getId() && !"".equals(questionandinfo.getId())) {
            sb.append(" and id='" + questionandinfo.getId() + "'");
        } else {
            if (null != questionandinfo.getSchoolno() && questionandinfo.getSchoolno().length() > 0 && !"noselected".equals(questionandinfo.getSchoolno())) {
                sb.append(" and schoolno='" + questionandinfo.getSchoolno() + "'");
            }
            if (null != questionandinfo.getGradeno() && questionandinfo.getGradeno().length() > 0 && !"noselected".equals(questionandinfo.getGradeno())) {
                sb.append(" and gradeno='" + questionandinfo.getGradeno() + "'");
            }
            if (null != questionandinfo.getSubjectno() && !"".equals(questionandinfo.getSubjectno())) {
                sb.append(" and subjectno='" + questionandinfo.getSubjectno() + "'");
            }
            if (null != questionandinfo.getQuestionid() && !"".equals(questionandinfo.getQuestionid())) {
                sb.append(" and questionid='" + questionandinfo.getQuestionid() + "'");
            }
            if (null != questionandinfo.getQuestiontype() && questionandinfo.getQuestiontype().length() > 0 && !"noselected".equals(questionandinfo.getQuestiontype())) {
                sb.append(" and questiontype='" + questionandinfo.getQuestiontype() + "'");
            }
            if (null != questionandinfo.getCreator() && !"".equals(questionandinfo.getCreator())) {
                sb.append(" and creator='" + questionandinfo.getCreator() + "'");
            }
            if (null != questionandinfo.getDifficulty() && !"".equals(questionandinfo.getDifficulty())) {
                sb.append(" and difficulty=" + questionandinfo.getDifficulty());
            }
            if (null != questionandinfo.getQuestion() && !"".equals(questionandinfo.getQuestion())) {
                sb.append(" and question like '%" + questionandinfo.getQuestion() + "%'");
            }
        }
//        sb.append(" order by questionid,questionno");

        return sb.toString();
    }

    @Override
    public int getTotalCount(VQuestionandinfo questionandinfo) {
        int res = 0;
        StringBuilder stringBuilder = new StringBuilder("select count(distinct QUESTIONID) ");
        stringBuilder.append(this.getHql(questionandinfo));
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(stringBuilder.toString());
        res = Integer.parseInt(query.uniqueResult().toString());
        return res;
    }
}
