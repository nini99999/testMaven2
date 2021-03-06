package com.extjs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jenny on 2017/4/9.
 */
@Entity
@Table(name = "R_CLASSMARK")


public class RClassMark {
    private String id;
    private String schoolno;
    private String gradeno;
    private String classno;
//    private String tpno;
    private String subjectno;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private Date testdate;
    private Float mark;
    private String creator;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdate;
    private String beginDate;
    private String endDate;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SCHOOLNO", nullable = true, length = 20)
    public String getSchoolno() {
        return schoolno;
    }

    public void setSchoolno(String schoolno) {
        this.schoolno = schoolno;
    }

    @Basic
    @Column(name = "GRADENO", nullable = true, length = 20)
    public String getGradeno() {
        return gradeno;
    }

    public void setGradeno(String gradeno) {
        this.gradeno = gradeno;
    }

    @Basic
    @Column(name = "CLASSNO", nullable = true, length = 20)
    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }

//    @Basic
//    @Column(name = "TPNO", nullable = true, length = 32)
//    public String getTpno() {
//        return tpno;
//    }
//
//    public void setTpno(String tpno) {
//        this.tpno = tpno;
//    }

    @Basic
    @Column(name = "SUBJECTNO", nullable = true, length = 20)
    public String getSubjectno() {
        return subjectno;
    }

    public void setSubjectno(String subjectno) {
        this.subjectno = subjectno;
    }
//
//    @Basic
//    @Column(name = "TESTDATE", nullable = true)
//    public Date getTestdate() {
//        return testdate;
//    }
//
//    public void setTestdate(Date testdate) {
//        this.testdate = testdate;
//    }

    @Basic
    @Column(name = "MARK", nullable = true, precision = 2)
    public Float getMark() {
        return mark;
    }

    public void setMark(Float mark) {
        this.mark = mark;
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

    @Transient
    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    @Transient
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}