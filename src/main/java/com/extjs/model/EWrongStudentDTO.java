package com.extjs.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.Date;

/**
 * Created by jenny on 2017/5/22.
 */
public class EWrongStudentDTO {
    private String id;
    private String countryid;
    private String testpaperno;
    private Integer questionno;
    private String creator;
    private Date createdate;
    private Date testdate;
    private String studentname;
    private String testpapername;
    private Boolean estate;
    private String testpoint;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "COUNTRYID", nullable = true, length = 20)
    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    @Basic
    @Column(name = "TESTPAPERNO", nullable = true, length = 20)
    public String getTestpaperno() {
        return testpaperno;
    }

    public void setTestpaperno(String testpaperno) {
        this.testpaperno = testpaperno;
    }

    @Basic
    @Column(name = "QUESTIONNO", nullable = true, length = 4)
    public Integer getQuestionno() {
        return questionno;
    }

    public void setQuestionno(Integer questionno) {
        this.questionno = questionno;
    }

    @Basic
    @Column(name = "CREATOR", nullable = true, length = 20)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Basic
    @Column(name = "CREATEDATE", nullable = true)
    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    @Basic
    @Column(name = "TESTDATE", nullable = true)
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
}
