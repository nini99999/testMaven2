package com.extjs.model;

public class EWrongKnowledgeDTO {
    private String questionID;
    private String studentID;
    private String knowledgeID;
    private String question;

    public String getKnowledgeID() {
        return knowledgeID;
    }

    public void setKnowledgeID(String knowledgeID) {
        this.knowledgeID = knowledgeID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
