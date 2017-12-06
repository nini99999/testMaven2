package com.extjs.model;

import javax.persistence.*;

@Entity
@Table(name = "V_TEACHER_CLASS")
public class VTeacherClass {
    private String id;
    private String subjectno;
    private String teacherid;
    private String teachername;
    private String classno;

    @Basic
    @Column(name = "SUBJECTNO", nullable = true, length = 20)
    public String getSubjectno() {
        return subjectno;
    }

    public void setSubjectno(String subjectno) {
        this.subjectno = subjectno;
    }

    @Basic
    @Column(name = "TEACHERID", nullable = true, length = 20)
    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    @Basic
    @Column(name = "TEACHERNAME", nullable = true, length = 20)
    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    @Basic
    @Column(name = "CLASSNO", nullable = false, length = 20)
    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }

    @Id
    @Basic
    @Column(name = "ID", nullable = false, length = 18)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
