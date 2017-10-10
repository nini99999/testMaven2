package com.extjs.service.impl;

import com.extjs.dao.EgradeDao;
import com.extjs.dao.EschoolDao;
import com.extjs.model.EGrade;
import com.extjs.model.EGradeDTO;
import com.extjs.model.ESchool;
import com.extjs.model.UserDTO;
import com.extjs.service.EgradeService;
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
 * Created by jenny on 2017/3/20.
 */
@Service
@Scope("prototype")
public class EgradeServiceImpl implements EgradeService {
    @Autowired
    private EgradeDao egradeDao;
    @Autowired
    private UserService userService;
    @Autowired
    private EschoolDao eschoolDao;

    @Override
    public List<EGradeDTO> queryEgrade() {
        List<EGradeDTO> eGradeDTOList = new ArrayList<EGradeDTO>();
        List<EGrade> eGradeList = egradeDao.queryEgrade();//执行查询
        EGradeDTO eGradeDTO = null;
        for (EGrade eGrade : eGradeList) {
            eGradeDTO = new EGradeDTO();
            ReflectionUtil.copyProperties(eGrade, eGradeDTO);//将实体类转化为DTO，以返回页面
            eGradeDTOList.add(eGradeDTO);
        }
        return eGradeDTOList;
    }

    /**
     * 查询年级列表，如果指定学校为空，则根据当前登录用户获取其所对应的学校；然后根据学校查询其对应的年级列表
     * @param schoolno
     * @return List<EGradeDTO>
     */
    public List<EGradeDTO> queryEgradeByschoolno(String schoolno) {
        List<EGradeDTO> eGradeDTOList = new ArrayList<EGradeDTO>();
        ESchool eSchool = new ESchool();
        if (schoolno == null || schoolno.equals("")) {
//             eGradeDTOList = egradeService.queryEgrade();
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            try {
                UserDTO userDTO = userService.getUserByUnique(userDetails.getUsername());
                eSchool.setId(userDTO.getuserSchool());
                eSchool = eschoolDao.queryEschool(eSchool);
                schoolno=eSchool.getSchoolno();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<EGrade> eGradeList = egradeDao.queryEgradeByschoolno(schoolno);//执行查询
        EGradeDTO eGradeDTO = null;
        for (EGrade eGrade : eGradeList) {
            eGradeDTO = new EGradeDTO();
            ReflectionUtil.copyProperties(eGrade, eGradeDTO);//将实体类转化为DTO，以返回页面
            eGradeDTOList.add(eGradeDTO);
        }
        return eGradeDTOList;
    }

    @Override
    public void addEgrade(EGradeDTO eGradeDTO) throws SysException {
        UUID uuid = UUID.randomUUID();
        Date date = new Date(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        eGradeDTO.setId(uuid.toString());
        eGradeDTO.setCreatedate(date);
        eGradeDTO.setCreator(userDetails.getUsername());
        egradeDao.addEgrade(eGradeDTO);//根据DTO进行添加

    }

    @Override
    public void delEgrade(EGradeDTO eGradeDTO) throws SysException {
        egradeDao.delEgrade(eGradeDTO);//根据DTO进行删除
    }
}
