package com.extjs.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2015/12/30.
 */
@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @GenericGenerator(name="uuid", strategy="uuid")
    @GeneratedValue(generator="uuid")
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "user_state")
    private String userState;
    @Column(name = "locked_state")
    private String lockedState;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "user_real_name")
    private String userRealName;
    @Column(name = "user_description")
    private String userDescription;
    @Column(name = "user_grade")
    private String userGrade;
    @Column(name = "user_role_ids")
    private String userRoleIds;
    @Column(name = "user_role_names")
    private String userRoleNames;
    @Column(name = "user_school")
    private String userSchool;


    public String  getuserSchool(){
        return userSchool;
    }
    public void setuserSchool(String userSchool){
        this.userSchool=userSchool;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getLockedState() {
        return lockedState;
    }

    public void setLockedState(String lockedState) {
        this.lockedState = lockedState;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(String userGrade) {
        this.userGrade = userGrade;
    }

    public String getUserRoleIds() {
        return userRoleIds;
    }

    public void setUserRoleIds(String userRoleIds) {
        this.userRoleIds = userRoleIds;
    }

    public String getUserRoleNames() {
        return userRoleNames;
    }

    public void setUserRoleNames(String userRoleNames) {
        this.userRoleNames = userRoleNames;
    }
}
