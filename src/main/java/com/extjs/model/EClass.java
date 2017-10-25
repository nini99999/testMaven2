package com.extjs.model;



import javax.persistence.*;
import java.util.Date;


/**
 * Created by jenny on 2017/3/22.
 */
@Entity
@Table(name = "E_CLASS")

public class EClass {
    private String id;
    private String classno;
    private String classname;
    private String gradeno;
    private String schoolno;
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
    @Column(name = "CLASSNO", nullable = false, length = 20)
    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }

    @Basic
    @Column(name = "CLASSNAME", nullable = false, length = 20)
    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    @Basic
    @Column(name = "GRADENO", nullable = false, precision = 0)
    public String getGradeno() {
        return gradeno;
    }

    public void setGradeno(String gradeno) {
        this.gradeno = gradeno;
    }

    @Basic
    @Column(name = "SCHOOLNO", nullable = false, precision = 0)
    public String getSchoolno() {
        return schoolno;
    }

    public void setSchoolno(String schoolno) {
        this.schoolno = schoolno;
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