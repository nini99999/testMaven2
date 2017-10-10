package com.extjs.controller;

import com.extjs.model.Role;
import com.extjs.model.RoleDTO;
import com.extjs.service.RoleService;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/16.
 */
@Controller
@RequestMapping("role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("/addRole")
    @ResponseBody
    public Map<String,Object> addRole(RoleDTO roleDTO){
        Map<String,Object> resultMap=new HashMap<String, Object>();
        try {
            roleService.addRole(roleDTO);
            resultMap.put("success",true);
            resultMap.put("msg","添加成功");
        } catch (SysException e) {
            resultMap.put("success",false);
            resultMap.put("msg","添加失败"+e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }
//    @RequestMapping("/saveRole")
//    @ResponseBody
//    public Map<String,Object> saveRole(RoleDTO roleDTO){
//        Map<String,Object> resultMap=new HashMap<String, Object>();
//        try {
//            roleService.saveRole(roleDTO);
//            resultMap.put("success",true);
//            resultMap.put("msg","保存成功");
//        } catch (SysException e) {
//            resultMap.put("success",false);
//            resultMap.put("msg","保存成功"+e.getMessage());
//            e.printStackTrace();
//        }
//        return resultMap;
//    }
}
