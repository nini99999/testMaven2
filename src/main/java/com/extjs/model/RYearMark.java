package com.extjs.model;

/**
 * Created by jenny on 2017/5/7.
 */

import javax.persistence.*;
import java.sql.Date;
//import java.sql.Date;

/**
 * Created by jenny on 2017/5/7.
 */
@Entity
@Table(name = "R_YEARMARK")
public class RYearMark{
    private String id;
    private String year;
    private String classno;
    private String teachername;
    private Float markone;
    private Float marktwo;
    private Float markthree;
    private Float markfour;
    private Float markfive;
    private Float marksix;
    private Float markseven;
    private Float markeight;
    private Float marknine;
    private Float markten;
    private Float markeleven;
    private Float marktwevle;
    private Float markmidterm;
    private Float markfinal;
    private Float avemark;
    private Float avepositon;
    private String creator;
    private Date createdate;
    private String subjectno;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "YEAR", nullable = true, precision = 0)
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Basic
    @Column(name = "CLASSNO", nullable = true, length = 20)
    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }

    @Basic
    @Column(name = "TEACHERNAME", nullable = true, length = 20)
    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    @Basic
    @Column(name = "MARKONE", nullable = true, precision = 2)
    public Float getMarkone() {
        return markone;
    }

    public void setMarkone(Float markone) {
        this.markone = markone;
    }

    @Basic
    @Column(name = "MARKTWO", nullable = true, precision = 2)
    public Float getMarktwo() {
        return marktwo;
    }

    public void setMarktwo(Float marktwo) {
        this.marktwo = marktwo;
    }

    @Basic
    @Column(name = "MARKTHREE", nullable = true, precision = 2)
    public Float getMarkthree() {
        return markthree;
    }

    public void setMarkthree(Float markthree) {
        this.markthree = markthree;
    }

    @Basic
    @Column(name = "MARKFOUR", nullable = true, precision = 2)
    public Float getMarkfour() {
        return markfour;
    }

    public void setMarkfour(Float markfour) {
        this.markfour = markfour;
    }

    @Basic
    @Column(name = "MARKFIVE", nullable = true, precision = 2)
    public Float getMarkfive() {
        return markfive;
    }

    public void setMarkfive(Float markfive) {
        this.markfive = markfive;
    }

    @Basic
    @Column(name = "MARKSIX", nullable = true, precision = 2)
    public Float getMarksix() {
        return marksix;
    }

    public void setMarksix(Float marksix) {
        this.marksix = marksix;
    }

    @Basic
    @Column(name = "MARKSEVEN", nullable = true, precision = 2)
    public Float getMarkseven() {
        return markseven;
    }

    public void setMarkseven(Float markseven) {
        this.markseven = markseven;
    }

    @Basic
    @Column(name = "MARKEIGHT", nullable = true, precision = 2)
    public Float getMarkeight() {
        return markeight;
    }

    public void setMarkeight(Float markeight) {
        this.markeight = markeight;
    }

    @Basic
    @Column(name = "MARKNINE", nullable = true, precision = 2)
    public Float getMarknine() {
        return marknine;
    }

    public void setMarknine(Float marknine) {
        this.marknine = marknine;
    }

    @Basic
    @Column(name = "MARKTEN", nullable = true, precision = 2)
    public Float getMarkten() {
        return markten;
    }

    public void setMarkten(Float markten) {
        this.markten = markten;
    }

    @Basic
    @Column(name = "MARKELEVEN", nullable = true, precision = 2)
    public Float getMarkeleven() {
        return markeleven;
    }

    public void setMarkeleven(Float markeleven) {
        this.markeleven = markeleven;
    }

    @Basic
    @Column(name = "MARKTWEVLE", nullable = true, precision = 2)
    public Float getMarktwevle() {
        return marktwevle;
    }

    public void setMarktwevle(Float marktwevle) {
        this.marktwevle = marktwevle;
    }

    @Basic
    @Column(name = "MARKMIDTERM", nullable = true, precision = 2)
    public Float getMarkmidterm() {
        return markmidterm;
    }

    public void setMarkmidterm(Float markmidterm) {
        this.markmidterm = markmidterm;
    }

    @Basic
    @Column(name = "MARKFINAL", nullable = true, precision = 2)
    public Float getMarkfinal() {
        return markfinal;
    }

    public void setMarkfinal(Float markfinal) {
        this.markfinal = markfinal;
    }

    @Basic
    @Column(name = "AVEMARK", nullable = true, precision = 2)
    public Float getAvemark() {
        return avemark;
    }

    public void setAvemark(Float avemark) {
        this.avemark = avemark;
    }

    @Basic
    @Column(name = "AVEPOSITON", nullable = true, precision = 2)
    public Float getAvepositon() {
        return avepositon;
    }

    public void setAvepositon(Float avepositon) {
        this.avepositon = avepositon;
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
    @Column(name = "SUBJECTNO", nullable = true, length = 20)
    public String getSubjectno() {
        return subjectno;
    }

    public void setSubjectno(String subjectno) {
        this.subjectno = subjectno;
    }
}
