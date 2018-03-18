package com.extjs.model;


import javax.persistence.*;
import java.util.Date;

/**
 * Created by jenny on 2017/4/4.
 */
@Entity
@Table(name = "E_PAPER_Q_TYPE")
public class EPaperQType {
    private String id;
    private String tpno;
    private String questiontype;
    private String creator;
    private Float mark;
    private String description;
    private Integer questionnum;
    private Date createdate;

    @Id
    @Column(name = "ID", nullable = true, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TPNO", nullable = false, length = 32)
    public String getTpno() {
        return tpno;
    }

    public void setTpno(String tpno) {
        this.tpno = tpno;
    }

    @Basic
    @Column(name = "QUESTIONTYPE", nullable = false, length = 20)
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
    @Column(name = "MARK", nullable = true)
    public Float getMark() {
        return mark;
    }

    public void setMark(Float mark) {
        this.mark = mark;
    }
    @Basic
    @Column(name = "DESCRIPTION", nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Basic
    @Column(name = "QUESTIONNUM", nullable = true)
    public Integer getQuestionnum() {
        return questionnum;
    }

    public void setQuestionnum(Integer questionnum) {
        this.questionnum = questionnum;
    }
}