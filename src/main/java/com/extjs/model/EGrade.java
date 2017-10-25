package com.extjs.model;

/**
 * Created by jenny on 2017/3/20.
 */

import javax.persistence.*;
import java.util.Date;


/**
 * Created by jenny on 2017/3/20.
 */
@Entity
@Table(name = "E_GRADE")

public class EGrade {
    private String id;
    private String gradename;
    private String gradeno;
    private String creator;
    private Date createdate;
    private String schoolno;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "GRADENAME", nullable = false, length = 20)
    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    @Basic
    @Column(name = "GRADENO", nullable = false, length = 20)
    public String getGradeno() {
        return gradeno;
    }

    public void setGradeno(String gradeno) {
        this.gradeno = gradeno;
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
    @Column(name = "SCHOOLNO", nullable = true)
    public String getSchoolno() {
        return schoolno;
    }

    public void setSchoolno(String schoolno) {
        this.schoolno = schoolno;
    }
}