package com.bottomup.android.models;

import java.io.Serializable;
import java.util.HashMap;

public class UserModel implements Serializable {

    String userName;
    String gender;
    String email;
    String address;
    String apply;

    public UserModel() {
    }

    public UserModel(HashMap map) {
        this.userName = (String) map.get("user_name");
        this.gender = (String) map.get("gender");
        this.email = (String) map.get("email");
        this.address = (String) map.get("address");
        this.apply = (String) map.get("apply");
    }

    public void setUserName(String val) {
        this.userName = val;
    }
    public String getUserName() {
        return userName;
    }

    public void setGender(String val) {
        this.gender = val;
    }
    public String getGender() {
        return gender;
    }

    public void setEmail(String val) {
        this.email = val;
    }
    public String getEmail() {
        return email;
    }

    public void setAddress(String val) {
        this.address = val;
    }
    public String getAddress() {
        return address;
    }

    public void setApply(String val) {
        this.apply = val;
    }
    public String getApply() {
        return apply;
    }
}

