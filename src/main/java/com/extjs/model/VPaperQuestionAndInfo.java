package com.extjs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jenny on 2017/10/17.
 */
@Entity
@Table(name = "V_PAPERQUESTIONANDINFO")
public class VPaperQuestionAndInfo {
    private String id;
    private String questionid;
    private String question;
    private Integer questionno;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createdate;
    private String creator;
    private Float difficulty;
    private String gradeno;
    private String questiontype;
    private String schoolno;
    private String subjectno;
    private String paperid;
    private Integer paperquestionno;
    private Float questionpoints;
//    private Date testdate;

    private String startDate;
    private String endDate;

    @Id
    @Basic
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "QUESTION", nullable = true, length = 3900)
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Basic
    @Column(name = "QUESTIONNO", nullable = true, precision = 0)
    public Integer getQuestionno() {
        return questionno;
    }

    public void setQuestionno(Integer questionno) {
        this.questionno = questionno;
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
    @Column(name = "CREATOR", nullable = true, length = 20)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Basic
    @Column(name = "DIFFICULTY", nullable = true, precision = 2)
    public Float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Float difficulty) {
        this.difficulty = difficulty;
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
    @Column(name = "QUESTIONTYPE", nullable = true, length = 20)
    public String getQuestiontype() {
        return questiontype;
    }

    public void setQuestiontype(String questiontype) {
        this.questiontype = questiontype;
    }

    @Basic
    @Column(name = "SCHOOLNO", nullable = true, length = 20)
    public String getSchoolno() {
        return schoolno;
    }

    public void setSchoolno(String schoolno) {
        this.schoolno = schoolno;
    }

    public String getSubjectno() {
        return subjectno;
    }

    @Basic
    @Column(name = "SUBJECTNO", nullable = true, length = 20)
    public void setSubjectno(String subjectno) {
        this.subjectno = subjectno;
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
    @Column(name = "PAPERQUESTIONNO", nullable = true, precision = 0)
    public Integer getPaperquestionno() {
        return paperquestionno;
    }

    public void setPaperquestionno(Integer paperquestionno) {
        this.paperquestionno = paperquestionno;
    }

    @Basic
    @Column(name = "QUESTIONPOINTS", nullable = true, precision = 2)
    public Float getQuestionpoints() {
        return questionpoints;
    }

    public void setQuestionpoints(Float questionpoints) {
        this.questionpoints = questionpoints;
    }

//    @Basic
//    @Column(name = "TESTDATE", nullable = true)
//    public Date getTestdate() {
//        return testdate;
//    }

//
//    public void setTestdate(Date testdate) {
//        this.testdate = testdate;
//    }

    @Transient
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Transient
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
