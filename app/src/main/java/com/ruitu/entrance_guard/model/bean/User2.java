package com.ruitu.entrance_guard.model.bean;

import java.io.Serializable;

public class User2 implements Serializable {
    private String username;
    private String password;

    private String age;
    private String gender;
    private String address;
    private String hoby;

    public User2() {

    }

    public User2(String username, String password, String age, String gender, String address, String hoby) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.hoby = hoby;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHoby() {
        return hoby;
    }

    public void setHoby(String hoby) {
        this.hoby = hoby;
    }
}
