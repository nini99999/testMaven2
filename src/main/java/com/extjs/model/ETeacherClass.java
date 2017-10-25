package com.extjs.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jenny on 2017/3/26.
 */
@Entity
@Table(name = "E_TEACHER_CLASS")
public class ETeacherClass {
    private String id;
    private String teacherno;
    private String classno;
    private String creator;
    private Date createdate;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TEACHERNO", nullable = true, length = 20)
    public String getTeacherno() {
        return teacherno;
    }

    public void setTeacherno(String teacherno) {
        this.teacherno = teacherno;
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

}