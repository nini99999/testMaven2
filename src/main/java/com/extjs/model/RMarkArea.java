package com.extjs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jenny on 2017/4/17.
 */
@Entity
@Table(name = "R_MARKAREA")
public class RMarkArea {
    private String id;
    private String gradeno;
    private String schoolno;
    private String classno;
    private String markarea;
    private Float markareanum;
    private String creator;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createdate;
    private String subjectno;

    @Id
    @Column(name = "ID", nullable = true, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "CLASSNO", nullable = true, length = 20)
    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }

    @Basic
    @Column(name = "MARKAREA", nullable = true, length = 10)
    public String getMarkarea() {
        return markarea;
    }

    public void setMarkarea(String markarea) {
        this.markarea = markarea;
    }

    @Basic
    @Column(name = "MARKAREANUM", nullable = true, precision = 2)
    public Float getMarkareanum() {
        return markareanum;
    }

    public void setMarkareanum(Float markareanum) {
        this.markareanum = markareanum;
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
    @Column(name = "SUBJECTNO", nullable = true, length = 10)
    public String getSubjectno() {
        return subjectno;
    }

    public void setSubjectno(String subjectno) {
        this.subjectno = subjectno;
    }
}
