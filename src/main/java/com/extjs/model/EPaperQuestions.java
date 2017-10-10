package com.extjs.model;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by jenny on 2017/6/20.
 */
@Entity
@Table(name = "E_PAPERQUESTIONS")
public class EPaperQuestions {
    private String id;
    private String paperid;
    private String questionid;
    private Float questionpoints;
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
    @Column(name = "PAPERID", nullable = true, length = 64)
    public String getPaperid() {
        return paperid;
    }

    public void setPaperid(String paperid) {
        this.paperid = paperid;
    }

    @Basic
    @Column(name = "QUESTIONID", nullable = true, length = 64)
    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    @Basic
    @Column(name = "QUESTIONPOINTS", nullable = true, precision = 2)
    public Float getQuestionpoints() {
        return questionpoints;
    }

    public void setQuestionpoints(Float questionpoints) {
        this.questionpoints = questionpoints;
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
