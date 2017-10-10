package com.extjs.model;

/**
 * Created by jenny on 2017/4/2.
 * 用于接收页面传递参数subjectno+实体类ESubject
 */
public class ESubjectQTypeVO {
    private String subjectno;
    private String[] questionType;

    public String getSubjectno() {
        return subjectno;
    }

    public void setSubjectno(String subjectno) {
        this.subjectno = subjectno;
    }

    public String[] getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String[] questionType) {
        this.questionType = questionType;
    }
}
