package com.extjs.dao;

import com.extjs.model.UserRoleKey;

/**
 * Created by Administrator on 2016/2/4.
 */
public interface UserRoleKeyDao {
    void addUserRole(UserRoleKey userRoleKey);
    void deleteUserRole(String userId);
}
