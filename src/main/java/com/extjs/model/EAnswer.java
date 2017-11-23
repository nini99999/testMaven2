package com.extjs.model;

import javax.persistence.*;

/**
 * Created by jenny on 2017/11/21.
 */
@Entity
@Table(name = "E_ANSWER")
public class EAnswer {
    private String id;
    private String answer;
    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Basic
    @Column(name = "ANSWER", nullable = true, length = 3800)
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
