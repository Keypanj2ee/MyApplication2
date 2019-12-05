package com.example.myapplication.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 *
 * 数据类，用于捕获从LoginRepository中检索到已登录用户的用户信息.
 */
public class LoggedInUser {

    private String userId;
    private String displayName;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
