package com.extjs.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jenny on 2017/3/24.
 */

@Entity
@Table(name = "E_STUDENT")
public class EStudent {
    private String id;
    private String chinaid;
    private String studentname;
    private String localid;
    private String schoolno;
    private String countryid;
    private String gradeno;
    private String classno;
    private String nation;
    private Date birthday;
    private Date admissiondate;
    private String schoolstate;
    private String creator;
    private Date createdate;
    private String studystate;
    private String username;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CHINAID", nullable = true, length = 30)
    public String getChinaid() {
        return chinaid;
    }

    public void setChinaid(String chinaid) {
        this.chinaid = chinaid;
    }

    @Basic
    @Column(name = "STUDENTNAME", nullable = false, length = 20)
    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    @Basic
    @Column(name = "LOCALID", nullable = true, length = 30)
    public String getLocalid() {
        return localid;
    }

    public void setLocalid(String localid) {
        this.localid = localid;
    }

    @Basic
    @Column(name = "SCHOOLNO", nullable = false, length = 20)
    public String getSchoolno() {
        return schoolno;
    }

    public void setSchoolno(String schoolno) {
        this.schoolno = schoolno;
    }

    @Basic
    @Column(name = "COUNTRYID", nullable = true, length = 40)
    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
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
    @Column(name = "CLASSNO", nullable = false, length = 20)
    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }

    @Basic
    @Column(name = "NATION", nullable = true, length = 20)
    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    @Basic
    @Column(name = "BIRTHDAY", nullable = true)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "ADMISSIONDATE", nullable = true)
    public Date getAdmissiondate() {
        return admissiondate;
    }

    public void setAdmissiondate(Date admissiondate) {
        this.admissiondate = admissiondate;
    }

    @Basic
    @Column(name = "SCHOOLSTATE", nullable = true, length = 8)
    public String getSchoolstate() {
        return schoolstate;
    }

    public void setSchoolstate(String schoolstate) {
        this.schoolstate = schoolstate;
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
    @Column(name = "STUDYSTATE", nullable = true, length = 4)
    public String getStudystate() {
        return studystate;
    }

    public void setStudystate(String studystate) {
        this.studystate = studystate;
    }
    @Basic
    @Column(name = "USERNAME", nullable = true, length = 20)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
