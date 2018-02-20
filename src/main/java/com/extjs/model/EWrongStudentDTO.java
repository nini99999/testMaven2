package com.extjs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by jenny on 2017/5/22.
 */
public class EWrongStudentDTO {
    private String id;
    private String countryid;
    private String testpaperno;
    private Integer questionno;
    private String creator;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date testdate;
    private String studentname;
    private String testpapername;
    private Boolean estate;
    private String testpoint;
    private String studentid;
    private String classno;
    private String questionid;
    private String reason;
    private String analysis;
    private String solution;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }


    public String getTestpaperno() {
        return testpaperno;
    }

    public void setTestpaperno(String testpaperno) {
        this.testpaperno = testpaperno;
    }


    public Integer getQuestionno() {
        return questionno;
    }

    public void setQuestionno(Integer questionno) {
        this.questionno = questionno;
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


    public Date getTestdate() {
        return testdate;
    }

    public void setTestdate(Date testdate) {
        this.testdate = testdate;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getTestpapername() {
        return testpapername;
    }

    public void setTestpapername(String testpapername) {
        this.testpapername = testpapername;
    }

    public Boolean getEstate() {
        return estate;
    }

    public void setEstate(Boolean estate) {
        this.estate = estate;
    }

    public String getTestpoint() {
        return testpoint;
    }

    public void setTestpoint(String testpoint) {
        this.testpoint = testpoint;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
