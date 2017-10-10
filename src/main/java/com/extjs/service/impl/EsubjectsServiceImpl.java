package com.extjs.service.impl;

import com.extjs.service.EsubjectQTypeService;
import com.extjs.util.ReflectionUtil;
import com.extjs.dao.EsubjectsDao;
import com.extjs.model.Esubjects;
import com.extjs.model.EsubjectsDTO;
import com.extjs.service.EsubjectsService;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.sql.Date;

/**
 * Created by jenny on 2017/3/16.
 */

@Service
@Scope("prototype")
public class EsubjectsServiceImpl implements EsubjectsService {
    @Autowired
    private EsubjectsDao esubjectsDao;


    @Override
    public List<EsubjectsDTO> queryEsubjectsList() {
        List<EsubjectsDTO> esubjectsDTOList = new ArrayList<EsubjectsDTO>();
        List<Esubjects> esubjectss = esubjectsDao.queryEsubjectsList();//执行数据库查询
        EsubjectsDTO esubjectsDTO = null;
        for (Esubjects esubjects : esubjectss) {//转换实体类为试图类以返回页面
            esubjectsDTO = new EsubjectsDTO();
            ReflectionUtil.copyProperties(esubjects, esubjectsDTO);
            esubjectsDTOList.add(esubjectsDTO);
        }

        return esubjectsDTOList;
    }

    @Override
    public HashMap<String, String> queryEsubjectMap() {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        List<Esubjects> esubjectsList = esubjectsDao.queryEsubjectsList();
        for (Esubjects esubjects : esubjectsList) {
            resultMap.put(esubjects.getSubjectno(), esubjects.getSubjectname());
        }
        return resultMap;
    }

    @Override
    public void addEsubjects(EsubjectsDTO esubjectsDTO) throws SysException {
        UUID uuid = UUID.randomUUID();
        esubjectsDTO.setId(uuid.toString());
        Date date = new Date(System.currentTimeMillis());
        esubjectsDTO.setCreatedate(date);
        //获取当前登陆用户信息
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        esubjectsDTO.setCreator(userDetails.getUsername());
        String sid = esubjectsDao.addEsubjects(esubjectsDTO);
    }

    /**
     * 删除时级联删除学科题型表
     *
     * @param esubjectsDTO
     * @throws SysException
     */
    @Override
    public void delEsubjects(EsubjectsDTO esubjectsDTO) throws SysException {

        esubjectsDao.delEsbujects(esubjectsDTO);
    }


}
