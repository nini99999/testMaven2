package com.extjs.model;


import org.apache.commons.lang3.StringEscapeUtils;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by jenny on 2017/6/20.
 */
@Entity
@Table(name = "E_QUESTIONS")
public class EQuestions {
    private String id;
    private String question;
//    private String answer;
//    private String qtype;
    private Integer questionno;
//    private String creator;
//    private Date createdate;
    private String questionid;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "QUESTION", nullable = true, length = 3600)
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {

        this.question = question;
    }

//    @Basic
//    @Column(name = "ANSWER", nullable = true, length = 2000)
//    public String getAnswer() {
//        return answer;
//    }
//
//    public void setAnswer(String answer) {
//        this.answer = answer;
//    }
//
//    @Basic
//    @Column(name = "QTYPE", nullable = true, length = 4)
//    public String getQtype() {
//        return qtype;
//    }
//
//    public void setQtype(String qtype) {
//        this.qtype = qtype;
//    }

    @Basic
    @Column(name = "QUESTIONNO", nullable = true, precision = 0)
    public Integer getQuestionno() {
        return questionno;
    }

    public void setQuestionno(Integer questionno) {
        this.questionno = questionno;
    }

//    @Basic
//    @Column(name = "CREATOR", nullable = true, length = 20)
//    public String getCreator() {
//        return creator;
//    }
//
//    public void setCreator(String creator) {
//        this.creator = creator;
//    }
//
//    @Basic
//    @Column(name = "CREATEDATE", nullable = true)
//    public Date getCreatedate() {
//        return createdate;
//    }
//
//    public void setCreatedate(Date createdate) {
//        this.createdate = createdate;
//    }
//
    @Basic
    @Column(name = "QUESTIONID", nullable = false, length = 64)
    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }


}