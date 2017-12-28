package com.extjs.model;

import javax.persistence.*;

@Entity
@Table(name = "E_QUESTION_KNOWLEDGE", schema = "C##TEST", catalog = "")
public class EQuestionKnowledge {
    private String id;
    private String questionid;
    private String knowledgeid;
    private String knowledgetext;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "QUESTIONID", nullable = false, length = 64)
    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    @Basic
    @Column(name = "KNOWLEDGEID", nullable = false, length = 64)
    public String getKnowledgeid() {
        return knowledgeid;
    }

    public void setKnowledgeid(String knowledgeid) {
        this.knowledgeid = knowledgeid;
    }

    @Basic
    @Column(name = "KNOWLEDGETEXT", nullable = true, length = 200)
    public String getKnowledgetext() {
        return knowledgetext;
    }

    public void setKnowledgetext(String knowledgetext) {
        this.knowledgetext = knowledgetext;
    }
}
