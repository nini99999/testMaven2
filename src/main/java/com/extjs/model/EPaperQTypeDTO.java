package com.extjs.model;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by jenny on 2017/4/4.
 */
public class EPaperQTypeDTO {
    private String id;
    private String tpno;
    private String questiontype;
    private String questiontypename;
    private String description;
    private Float mark;
    private Integer questionnum;
    private String creator;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createdate;
    private String estate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTpno() {
        return tpno;
    }

    public void setTpno(String tpno) {
        this.tpno = tpno;
    }


    public String getQuestiontype() {
        return questiontype;
    }

    public void setQuestiontype(String questiontype) {
        this.questiontype = questiontype;
    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getEstate() {
        return estate;
    }

    public void setEstate(String estate) {
        this.estate = estate;
    }

    public String getQuestiontypename() {
        return questiontypename;
    }

    public void setQuestiontypename(String questiontypename) {
        this.questiontypename = questiontypename;
    }

    public Integer getQuestionnum() {
        return questionnum;
    }

    public void setQuestionnum(Integer questionnum) {
        this.questionnum = questionnum;
    }

    public Float getMark() {
        return mark;
    }

    public void setMark(Float mark) {
        this.mark = mark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
