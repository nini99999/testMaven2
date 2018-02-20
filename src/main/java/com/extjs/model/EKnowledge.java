package com.extjs.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "E_KNOWLEDGE")
public class EKnowledge {
    private String id;
    private String gradeno;
    private String subjectno;
    private String knowledgeText;
    private String parentid;
    private Integer haschild;
    private String creator;
    private Date createdate;
    private boolean onChecked;
    private Float grasping;
//    private List<EKnowledge> children;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "SUBJECTNO", nullable = true, length = 20)
    public String getSubjectno() {
        return subjectno;
    }

    public void setSubjectno(String subjectno) {
        this.subjectno = subjectno;
    }

    @Basic
    @Column(name = "KNOWLEDGE", nullable = false, length = 200)
    public String getKnowledgeText() {
        return knowledgeText;
    }

    public void setKnowledgeText(String knowledgeText) {
        this.knowledgeText = knowledgeText;
    }

    @Basic
    @Column(name = "PARENTID", nullable = false, length = 64)
    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    @Basic
    @Column(name = "HASCHILD", nullable = false)
    public Integer getHaschild() {
        return haschild;
    }

    public void setHaschild(Integer haschild) {
        this.haschild = haschild;
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

    @Transient
    public boolean isOnChecked() {
        return onChecked;
    }

    public void setOnChecked(boolean onChecked) {
        this.onChecked = onChecked;
    }

    @Transient
    public Float getGrasping() {
        return grasping;
    }

    public void setGrasping(Float grasping) {
        this.grasping = grasping;
    }

//    @Transient
//    public List<EKnowledge> getChildren() {
//        return children;
//    }
//
//    public void setChildren(List<EKnowledge> children) {
//        this.children = children;
//    }
}
