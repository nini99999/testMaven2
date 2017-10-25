package com.extjs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "E_STUDENT_MARK")
public class EStudentMark {
    private String id;
    private String studentname;
    private String tpname;
    private Float mark;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date testdate;
    private Float markone;
    private Float marktwo;
    private String creator;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createdate;
    private String subjectno;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "STUDENTNAME", nullable = true, length = 20)
    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    @Basic
    @Column(name = "TPNAME", nullable = true, length = 20)
    public String getTpname() {
        return tpname;
    }

    public void setTpname(String tpname) {
        this.tpname = tpname;
    }

    @Basic
    @Column(name = "MARK", nullable = true, precision = 2)
    public Float getMark() {
        return mark;
    }

    public void setMark(Float mark) {
        this.mark = mark;
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
    @Column(name = "MARKONE", nullable = true, precision = 2)
    public Float getMarkone() {
        return markone;
    }

    public void setMarkone(Float markone) {
        this.markone = markone;
    }

    @Basic
    @Column(name = "MARKTWO", nullable = true, precision = 2)
    public Float getMarktwo() {
        return marktwo;
    }

    public void setMarktwo(Float marktwo) {
        this.marktwo = marktwo;
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
    @Column(name = "SUBJECTNO", nullable = true, length = 20)
    public String getSubjectno() {
        return subjectno;
    }

    public void setSubjectno(String subjectno) {
        this.subjectno = subjectno;
    }
}