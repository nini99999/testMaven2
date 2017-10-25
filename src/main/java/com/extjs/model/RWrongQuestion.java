package com.extjs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by jenny on 2017/5/3.
 */
@Entity
@Table(name = "R_WRONGQUESTION")
public class RWrongQuestion {
    private String id;
    private String tpno;
    private String questionno;
    private Integer wrongnums;
    private Integer testnums;
    private String creator;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createdate;
    private String testpoint;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TPNO", nullable = true, length = 20)
    public String getTpno() {
        return tpno;
    }

    public void setTpno(String tpno) {
        this.tpno = tpno;
    }

    @Basic
    @Column(name = "QUESTIONNO", nullable = true, length = 20)
    public String getQuestionno() {
        return questionno;
    }

    public void setQuestionno(String questionno) {
        this.questionno = questionno;
    }

    @Basic
    @Column(name = "WRONGNUMS", nullable = true, precision = 0)
    public Integer getWrongnums() {
        return wrongnums;
    }

    public void setWrongnums(Integer wrongnums) {
        this.wrongnums = wrongnums;
    }

    @Basic
    @Column(name = "TESTNUMS", nullable = true, precision = 0)
    public Integer getTestnums() {
        return testnums;
    }

    public void setTestnums(Integer testnums) {
        this.testnums = testnums;
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
    @Column(name = "TESTPOINT", nullable = true,length = 100)
    public String getTestpoint() {
        return testpoint;
    }

    public void setTestpoint(String testpoint) {
        this.testpoint = testpoint;
    }
}
