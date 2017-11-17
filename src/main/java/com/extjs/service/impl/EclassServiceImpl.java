package com.extjs.service.impl;

import com.extjs.dao.EclassDao;
import com.extjs.dao.EgradeDao;
import com.extjs.model.EClass;
import com.extjs.model.EClassDTO;
import com.extjs.model.EGrade;
import com.extjs.model.EGradeDTO;
import com.extjs.service.EclassService;
import com.extjs.service.EgradeService;
import com.extjs.service.EschoolService;
import com.extjs.util.ReflectionUtil;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by jenny on 2017/3/22.
 */
@Service
@Scope("prototype")
public class EclassServiceImpl implements EclassService {

    @Autowired
    private EschoolService eschoolService;

    @Autowired
    private EclassDao eclassDao;
    @Autowired
    private EgradeDao egradeDao;

    @Override
    public List<EClassDTO> queryEclass() {
        List<EClassDTO> eClassDTOList = new ArrayList<EClassDTO>();
        List<EClass> eClassList = eclassDao.queryEclass();
        EClassDTO eClassDTO = null;
        for (EClass eClass : eClassList) {
            eClassDTO = new EClassDTO();
            ReflectionUtil.copyProperties(eClass, eClassDTO);
            eClassDTOList.add(eClassDTO);

        }
        return eClassDTOList;
    }

    @Override
    public HashMap<String, String> queryEclassMap() {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        List<EClass> eClassList = eclassDao.queryEclass();
        for (EClass eClass : eClassList) {
            resultMap.put(eClass.getClassno(), eClass.getClassname());
        }
        return resultMap;
    }

    @Override
    public List<EClassDTO> queryEclassByDTO(EClassDTO eClassDTO) {
        List<EClassDTO> eClassDTOList = new ArrayList<EClassDTO>();
        if (null == eClassDTO.getSchoolno() || eClassDTO.getSchoolno().length() == 0) {
            try {
                eClassDTO.setSchoolno(eschoolService.getSchoolnoByContext());
            } catch (SysException e) {
                e.printStackTrace();
            }
        }
        List<EClass> eClassList = eclassDao.queryEclass(eClassDTO);
//         eClassDTO = null;
        for (EClass eClass : eClassList) {
            eClassDTO = new EClassDTO();
            ReflectionUtil.copyProperties(eClass, eClassDTO);
            eClassDTOList.add(eClassDTO);

        }
        return eClassDTOList;
    }

    @Override

    public String addEclass(EClassDTO eClassDTO) throws SysException {
        EGrade eGrade = egradeDao.queryEgradeByID(eClassDTO.getGradeno());//根据年级编码获取年级名称

        UUID uuid = UUID.randomUUID();
        Date date = new Date(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if (null == eClassDTO.getSchoolno() || eClassDTO.getSchoolno().length() == 0) {
            eClassDTO.setSchoolno(eschoolService.getSchoolnoByContext());
        }
        eClassDTO.setId(uuid.toString());
        eClassDTO.setCreatedate(date);
        eClassDTO.setCreator(userDetails.getUsername());
        eClassDTO.setClassno(eClassDTO.getGradeno() + "-" + eClassDTO.getClassno());
        eClassDTO.setClassname(eGrade.getGradename() + "-" + eClassDTO.getClassname());//设置班级名称格式：年级名称-班级名称
        String flag = eclassDao.addEclass(eClassDTO);
        return flag;
    }

    @Override
    public void delEclass(EClassDTO eClassDTO) throws SysException {
        eclassDao.delEclass(eClassDTO);

    }
}
