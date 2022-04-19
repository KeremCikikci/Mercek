package com.example.mercek;

public class UserModel {

    private String userName;
    private String userMail;

    private UserModel(){
    }

    private UserModel(String userName, String userMail){
        this.userName = userName;
        this.userMail = userMail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
}
