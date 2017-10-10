package com.extjs.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/14.
 */
public class UserDTO {
    private String userId;
    private String userName;
    private String userPassword;
    private String userPassWord;
    private String userState;
    private String lockedState;
    private String email;
    private String phone;
    private String createTime;
    private String userRealName;
    private String userDescription;
    private String userGrade;
    private String userRoleIds;
    private String userRoleNames;
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

    public String getUserPassWord() {
        return userPassWord;
    }

    public void setUserPassWord(String userPassWord) {
        this.userPassWord = userPassWord;
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

