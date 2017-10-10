package com.extjs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2015/12/31.
 */
@Controller
@RequestMapping("/spring-security")
public class HelloController {


    @RequestMapping("/welcome")
    public String welcome(Model model){
        model.addAttribute("title","welcome");
        model.addAttribute("message","welcome");
        return "loginlogout/hello";
    }
    @RequestMapping("/admin")
    public String admin(Model model){
        model.addAttribute("title","admin");
        model.addAttribute("message","admin");
        return "loginlogout/admin";
    }
    @RequestMapping("/login")
    public String login(String error,String logout,Model model){
        System.out.println("error:"+error);
        if(error!=null&&"".equals(error)){
            model.addAttribute("error","invalid username and password");
        }
        if (logout!=null&&"".equals(logout)){
            model.addAttribute("msg","logged out");
        }
        return "loginlogout/login";
    }
}
