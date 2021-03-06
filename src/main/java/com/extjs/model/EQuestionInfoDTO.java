package com.extjs.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by jenny on 2017/6/22.
 */
public class EQuestionInfoDTO {
    private String id;
    private String questionid;
    private String questiontype;

    private String creator;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createdate;

    private String gradeno;
    private String subjectno;
    private Float difficulty;
    private String schoolno;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }


    public String getQuestiontype() {
        return questiontype;
    }

    public void setQuestiontype(String questiontype) {
        this.questiontype = questiontype;
    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getGradeno() {
        return gradeno;
    }

    public void setGradeno(String gradeno) {
        this.gradeno = gradeno;
    }

    public String getSubjectno() {
        return subjectno;
    }

    public void setSubjectno(String subjectno) {
        this.subjectno = subjectno;
    }

    public Float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Float difficulty) {
        this.difficulty = difficulty;
    }

    public String getSchoolno() {
        return schoolno;
    }

    public void setSchoolno(String schoolno) {
        this.schoolno = schoolno;
    }
}
