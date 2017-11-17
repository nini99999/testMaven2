package com.extjs.service.impl;

import com.extjs.dao.EschoolDao;
import com.extjs.model.ESchoolDTO;
import com.extjs.model.ESchool;
import com.extjs.service.EschoolService;
import com.extjs.service.UserService;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jenny on 2017/3/18.
 */
@Service
@Scope("prototype")
public class EschoolServiceImpl implements EschoolService {
    @Autowired
    private EschoolDao eschoolDao;
    @Autowired
    private UserService userService;


    @Override
    public List<ESchoolDTO> queryEschool() {
        List<ESchoolDTO> eSchoolDTOList = new ArrayList<ESchoolDTO>();
        List<ESchool> eSchoolList = eschoolDao.queryEschool();//执行查询
        ESchoolDTO eSchoolDTO = null;
        for (ESchool eSchool : eSchoolList) {
            eSchoolDTO = new ESchoolDTO();
            ReflectionUtil.copyProperties(eSchool, eSchoolDTO);//转换实体类为试图类以返回页面
            eSchoolDTOList.add(eSchoolDTO);
        }
        return eSchoolDTOList;
    }

    @Override
    public ESchoolDTO querySchoolByUnique(String id,String schoolno) {
        ESchool school=new ESchool();
        school.setId(id);
        school.setSchoolno(schoolno);
        school=eschoolDao.queryEschool(school);
        ESchoolDTO schoolDTO=new ESchoolDTO();
        ReflectionUtil.copyProperties(school,schoolDTO);
        return schoolDTO;
    }

    @Override
    public void addEschool(ESchoolDTO eSchoolDTO) throws SysException {
        UUID uuid = UUID.randomUUID();
        eSchoolDTO.setId(uuid.toString());
        Date date=new Date(System.currentTimeMillis());
        eSchoolDTO.setCreatedate(date);
        //获取当前登陆用户信息
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        eSchoolDTO.setCreator(userDetails.getUsername());
        String sid=eschoolDao.addEschool(eSchoolDTO);

    }
    @Override
    public void delEschool(ESchoolDTO eSchoolDTO) throws SysException {
        eschoolDao.delEschool(eSchoolDTO);
    }

    @Override
    public String getSchoolnoByContext() throws SysException{

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String schoolid = userService.getUserByUnique(userDetails.getUsername()).getuserSchool();
        ESchoolDTO schoolDTO = this.querySchoolByUnique(schoolid, null);
        return schoolDTO.getSchoolno();
    }
}
