package com.extjs.service.impl;

import com.extjs.dao.EStudentMarkDao;
import com.extjs.model.EStudentMark;
import com.extjs.model.ETestpaper;
import com.extjs.model.ETestpaperDTO;
import com.extjs.model.Page;
import com.extjs.service.EstudentMarkService;
import com.extjs.service.EtestpaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
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

    @Override
    public List<EStudentMark> queryEStudentMark(EStudentMark eStudentMark, Page page) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
//        Date date = new Date(System.currentTimeMillis());
        eStudentMark.setCreator(userDetails.getUsername());
//        eStudentMark.setCreatedate(date);
        List<EStudentMark> studentMarks = studentMarkDao.queryEStudentMark(eStudentMark,page);
        return studentMarks;
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
            List<ETestpaperDTO> testpaperDTOList = etestpaperService.queryEtestpaper(testpaperDTO);
            for (ETestpaperDTO eTestpaperDTO : testpaperDTOList) {
                eStudentMark.setTpname(eTestpaperDTO.getTpname());
                eStudentMark.setTestdate(eTestpaperDTO.getTestdate());
            }
        }


//        if (null == eStudentMark.getId() || eStudentMark.getId().length() == 0) {
        List<EStudentMark> studentMarkLis = this.queryEStudentMark(eStudentMark,null);
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
     int res=   studentMarkDao.getTotalCount(eStudentMark);
     return res;
    }
}
