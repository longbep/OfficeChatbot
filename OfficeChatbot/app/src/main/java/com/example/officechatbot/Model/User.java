package com.example.officechatbot.Model;

public class User {
    private String email;
    private String name;
    private String phone;
    private String avatar;
    private String idNumber;
    private String gender;
    private String dateOfBirth;

    public User(String email, String name, String phone, String avatar, String idNumber, String gender, String dateOfBirth) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
        this.idNumber = idNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public User() {

    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
