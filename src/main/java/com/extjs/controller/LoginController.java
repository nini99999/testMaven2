package com.extjs.controller;

import com.extjs.model.UserDTO;
import com.extjs.service.UserService;
import com.extjs.util.DESUtil;
import com.extjs.util.SysException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/2/19.
 */
@RequestMapping("loginlogout")
@Controller
public class LoginController {
    private static Log logger= LogFactory.getLog(LoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "jsp/login";
    }
    @RequestMapping("/gotoDeniedPage")
    public String gotoDeniedPage(){
        return "jsp/denied";
    }

    @RequestMapping("/login")
    @ResponseBody
    public Map<String,Object> login(String name,String passwd,HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap<String, Object>();
        logger.debug("name:"+name);
        passwd= DESUtil.md5(passwd);
        try {
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(name,passwd);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession().setAttribute("SPRING_SECURITY_CONTEXT",SecurityContextHolder.getContext());

            String name1=authentication.getName();
            logger.debug("Name:"+name1);
            resultMap.put("success", true);
            resultMap.put("msg","登录成功!");
        } catch (AuthenticationException e) {
            resultMap.put("success",false);
            resultMap.put("msg","用户名或密码错误,请重新登录!");
            e.printStackTrace();
        }

//        try {
//            List<UserDTO> userDTOs = userService.getUser(name);
//            if (userDTOs!=null&&userDTOs.size()>0){
//                UserDTO userDTO = userDTOs.get(0);
//                if (!userDTO.getUserPassword().equals(passwd)){
//                    resultMap.put("success",false);
//                    resultMap.put("msg","密码错误!");
//                }else {
//                    request.getSession().setAttribute("userDTO",userDTO);
//                    resultMap.put("success",true);
//                    resultMap.put("msg","登录成功!");
//                }
//            }else {
//                resultMap.put("success",false);
//                resultMap.put("msg","用户不存在!");
//            }
//
//        } catch (SysException e) {
//            e.printStackTrace();
//            resultMap.put("success",false);
//            resultMap.put("msg","系统错误!"+e.getMessage());
//        }
        return resultMap;
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("userDTO");
        return "redirect:toLogin";
    }


}
