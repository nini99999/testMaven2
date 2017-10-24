package com.extjs.model;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by jenny on 2017/6/20.
 */

public class EPaperQuestionsDTO {
    private String id;
    private String paperid;
    private String questionid;
    private Float questionpoints;
//    private String creator;
//    private Date createdate;
    private String question;
    private Integer questionno;
    private Integer paperquestionno;
//    private Date testdate;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPaperid() {
        return paperid;
    }

    public void setPaperid(String paperid) {
        this.paperid = paperid;
    }


    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }


    public Float getQuestionpoints() {
        return questionpoints;
    }

    public void setQuestionpoints(Float questionpoints) {
        this.questionpoints = questionpoints;
    }


//    public String getCreator() {
//        return creator;
//    }
//
//    public void setCreator(String creator) {
//        this.creator = creator;
//    }
//
//
//    public Date getCreatedate() {
//        return createdate;
//    }
//
//    public void setCreatedate(Date createdate) {
//        this.createdate = createdate;
//    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public Integer getQuestionno() {
        return questionno;
    }

    public void setQuestionno(Integer questionno) {
        this.questionno = questionno;
    }


    public Integer getPaperquestionno() {
        return paperquestionno;
    }

    public void setPaperquestionno(Integer paperquestionno) {
        this.paperquestionno = paperquestionno;
    }

//    public Date getTestdate() {
//        return testdate;
//    }
//
//    public void setTestdate(Date testdate) {
//        this.testdate = testdate;
//    }

}
