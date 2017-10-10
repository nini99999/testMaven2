package com.extjs.model;

import java.util.List;

/**
 * Created by jenny on 2017/3/29.
 * 用于处理接收jsp页面传递的参数，包括teacherid和实体类EClassDTO
 */
public class EteacherClassVO {
    private String teacherid;
    private String[] classno;



    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String[] getClassno() {
        return classno;
    }

    public void setClassno(String[] classno) {
        this.classno = classno;
    }
}
