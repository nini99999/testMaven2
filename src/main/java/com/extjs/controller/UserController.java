package com.extjs.controller;

import com.extjs.model.*;
import com.extjs.service.EschoolService;
import com.extjs.service.UserService;
import com.extjs.util.DESUtil;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.io.UnsupportedEncodingException;
/**
 * Created by Administrator on 2016/2/4.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EschoolService eschoolService;
    @RequestMapping("/toViewUserList")
    public String toViewUserList(){
        return "jsp/viewUserList";
    }

    @RequestMapping("/viewUserList")
    @ResponseBody
    public Map queryUserList(){
        Map<String,Object> resultMap=new HashMap<String, Object>();
        List<User> users = userService.queryUserList();
        resultMap.put("data",users);
        resultMap.put("total",users.size());
        return  resultMap;
    }

    @RequestMapping("/toViewRoleList")
    public String toViewRoleList(){
        return "jsp/viewRoleList";
    }

    @RequestMapping("/viewRoleList")
    @ResponseBody
    public Map queryRoleList(){
        Map<String,Object> resultMap=new HashMap<String, Object>();
        List<RoleDTO> roleDTOs = userService.queryRoleList();
        resultMap.put("data",roleDTOs);
        resultMap.put("total",roleDTOs.size());
        return  resultMap;
    }
    @RequestMapping("/querySchool")
    @ResponseBody
    public void querySchool (HttpServletResponse response) throws UnsupportedEncodingException {
        List<ESchoolDTO> eSchoolDTOList = eschoolService.queryEschool();
        Collection<DirctDTO> list=new ArrayList<DirctDTO>();
        for (ESchoolDTO school:eSchoolDTOList){
            // list.add(new String[]{school.getId(),school.getSchoolname()});
            list.add(new DirctDTO(school.getId(),school.getSchoolname()));
        }
        JSONArray jsonArr = new JSONArray(list.toArray());
        response.setContentType("text/html;charset=utf-8");
        String str=jsonArr.toString();
        try {
            PrintWriter out = response.getWriter();
            out.print(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return str ;
    }

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> userAdd(UserDTO userDTO){
        System.out.println("userRoleIds:"+userDTO.getUserRoleIds());
         userDTO.setUserPassword(DESUtil.md5(userDTO.getUserPassword()));
        Map<String,Object> resultMap=new HashMap<String, Object>();
        try {
            userService.addUser(userDTO);
            resultMap.put("success",true);
            resultMap.put("msg","添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success",false);
            resultMap.put("msg","添加失败!"+e.getMessage());
        }
        return  resultMap;
    }
    @RequestMapping(value = "/deleteUser",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteUser(String  userId){

        Map<String,Object> resultMap=new HashMap<String, Object>();
        try {
            userService.deleteUser(userId);
            resultMap.put("success",true);
            resultMap.put("msg","删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success",false);
            resultMap.put("msg","删除失败!"+e.getMessage());
        }
        return  resultMap;
    }
    @RequestMapping(value = "/getUserRealName",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getUserRealName(HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap<String, Object>();
        UserDTO userDTO = (UserDTO) request.getSession().getAttribute("userDTO");

        if (userDTO.getUserRealName()!=null&&!"".equals(userDTO.getUserRealName())){
            resultMap.put("success",true);
            resultMap.put("data",userDTO.getUserRealName());
        }else {
            resultMap.put("success",false);
            resultMap.put("data",userDTO.getUserRealName());
        }
        return resultMap;
    }
}
