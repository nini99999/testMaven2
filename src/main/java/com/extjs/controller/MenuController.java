package com.extjs.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.extjs.dao.MenuDao;
import com.extjs.model.MenuDTO;
import com.extjs.model.UserDTO;
import com.extjs.service.UserService;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.extjs.service.MenuService;
import com.extjs.util.JSONTreeNode;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2015/12/25.
 */
@Controller
@RequestMapping("menu")
public class MenuController {

   @Autowired
   private  MenuService menuService;
    @Autowired
    private UserService userService;

    @RequestMapping("/module1")
    public String toModule1(){
        return "jsp/module1_1";
    }
    @RequestMapping("/module2")
    public String toModule2(){
        return "jsp/module2";
    }

    @RequestMapping("/toDemo")
    public String toDemo(){
        return "jsp/demo";
    }

    @RequestMapping("/viewMenuList")
    public String viewMenuList(){
        return "jsp/viewMenuList";
    }
    @RequestMapping("/addMenu")
    @ResponseBody
    public Map<String,Object> addMenu(MenuDTO menuDTO){
        Map<String,Object> resultMap=new HashMap<String, Object>();
        try {
            menuService.addMenu(menuDTO);
            resultMap.put("success",true);
            resultMap.put("msg","新增菜单成功!");
        } catch (SysException e) {
            resultMap.put("success",false);
            resultMap.put("msg","新增菜单失败!"+e.getMessage());
            e.printStackTrace();
        }
        return  resultMap;
    }

    @RequestMapping("/delMenu")
    @ResponseBody
    public Map<String,Object> delMenu(String menuId){
        Map<String,Object> resultMap=new HashMap<String, Object>();
        try {
            menuService.delMenu(menuId);
            resultMap.put("success",true);
            resultMap.put("msg","删除菜单成功!");
        } catch (SysException e) {
            resultMap.put("success",true);
            resultMap.put("msg","删除菜单失败!"+e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }



    @RequestMapping("/toMenu")
    public void getMenuList(HttpServletResponse response){
        List<JSONTreeNode> menuJSONTreeNode = menuService.getMenuJSONTreeNode();
        for (JSONTreeNode jsonTreeNode : menuJSONTreeNode) {
            System.out.println(jsonTreeNode.getText());
            List<JSONTreeNode> children = jsonTreeNode.getChildren();
            if (children!=null&&children.size()>0){
                for (JSONTreeNode child : children) {
                    System.out.print(child.getText()+"\t");
                }
            }
            System.out.println();
        }
        Map<String,Object> result=new HashMap<String, Object>();
        result.put("expanded",false);
        result.put("children",menuJSONTreeNode);
        JSONObject jsonObject=new JSONObject(result);
        System.out.println(jsonObject.toJSONString());
        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter out = response.getWriter();
            out.print(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/toUserMenu")
    @ResponseBody
    public Map<String,Object> getMenuListByUserRole()  {
        Map<String,Object> resultMap=new HashMap<String, Object>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails=null;
        List<JSONTreeNode> menuJSONTreeNode=null;
        if (principal instanceof UserDetails){
            userDetails= (UserDetails) principal;
        }
        List<UserDTO> userDTOs=null;
        try {
            userDTOs = userService.getUser(userDetails.getUsername());
            if (userDTOs!=null&&userDTOs.size()>0){
                UserDTO userDTO = userDTOs.get(0);
                String userRoleIds = userDTO.getUserRoleIds();
                String[] roleIds = {""};
                if (userRoleIds!=null){
                roleIds = userRoleIds.split(",");
                }
                menuJSONTreeNode = menuService.getMenuByUserRole(roleIds);
            }
        } catch (SysException e) {
            e.printStackTrace();
        }
        resultMap.put("expanded",false);
        resultMap.put("children",menuJSONTreeNode);

        return resultMap;
    }
}
