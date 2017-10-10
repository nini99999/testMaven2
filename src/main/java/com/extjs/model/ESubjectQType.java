package com.extjs.model;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by jenny on 2017/4/2.
 */
@Entity
@Table(name = "E_SUBJECT_Q_TYPE")
public class ESubjectQType {
    private String id;
    private String subjectno;
    private String questiontype;
    private String creator;
    private Date createdate;
    private String questiontypename;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "QUESTIONTYPE", nullable = true, length = 20)
    public String getQuestiontype() {
        return questiontype;
    }

    public void setQuestiontype(String questiontype) {
        this.questiontype = questiontype;
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
    @Column(name = "QUESTIONTYPENAME", nullable = true, length = 30)
    public String getQuestiontypename() {
        return questiontypename;
    }

    public void setQuestiontypename(String questiontypename) {
        this.questiontypename = questiontypename;
    }


    @Basic
    @Column(name = "SUBJECTNO", nullable = false, length = 20)
    public String getSubjectno() {
        return subjectno;
    }

    public void setSubjectno(String subjectno) {
        this.subjectno = subjectno;
    }
}
