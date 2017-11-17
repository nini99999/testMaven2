package com.extjs.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jenny on 2017/5/22.
 */
@Entity
@Table(name = "E_WRONG_STUDENT")
public class EWrongStudent {
    private String id;
    private String countryid;
    private String testpaperno;
    private Integer questionno;
    private String creator;
    private Date createdate;
    private Date testdate;
    private String testpoint;
    private String studentid;

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
    @Column(name = "TESTPAPERNO", nullable = true, length = 32)
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

    @Basic
    @Column(name = "testpoint", nullable = true,length = 128)
    public String getTestpoint() {
        return testpoint;
    }

    public void setTestpoint(String testpoint) {
        this.testpoint = testpoint;
    }
    @Basic
    @Column(name = "studentid", nullable = true,length = 64)
    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }
}