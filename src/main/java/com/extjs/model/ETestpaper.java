package com.extjs.model;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by jenny on 2017/4/4.
 */
@Entity
@Table(name = "E_TESTPAPER")
public class ETestpaper {
    private String id;

    private String tpname;
    private String creator;
    private Date createdate;
    private String subjectno;
    private String gradeno;
    private String schoolno;
    private String term;
    private String tpno;
    private String examtype;
    private Date testdate;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Basic
    @Column(name = "TPNAME", nullable = false, length = 128)
    public String getTpname() {
        return tpname;
    }

    public void setTpname(String tpname) {
        this.tpname = tpname;
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

    @Basic
    @Column(name = "GRADENO", nullable = true, length = 20)
    public String getGradeno() {
        return gradeno;
    }

    public void setGradeno(String gradeno) {
        this.gradeno = gradeno;
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
    @Column(name = "TERM", nullable = true, length = 4)
    public String getTerm() {
        return term;
    }


    public void setTerm(String term) {
        this.term = term;
    }

    @Basic
    @Column(name = "TPNO", nullable = true, length = 32)
    public String getTpno() {
        return tpno;
    }

    public void setTpno(String tpno) {
        this.tpno = tpno;
    }

    @Basic
    @Column(name = "EXAMTYPE", nullable = true, length = 1)
    public String getExamtype() {
        return examtype;
    }

    public void setExamtype(String examtype) {
        this.examtype = examtype;
    }

    @Basic
    @Column(name = "TESTDATE", nullable = true)
    public Date getTestdate() {
        return testdate;
    }

    public void setTestdate(Date testdate) {
        this.testdate = testdate;
    }
}
