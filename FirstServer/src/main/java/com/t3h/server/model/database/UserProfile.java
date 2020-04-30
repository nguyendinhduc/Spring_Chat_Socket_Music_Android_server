package com.t3h.server.model.database;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    private String username;
    private String password;
    private String avatar;
    private String dob;
//    private LocalDateTime createdTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

//    public LocalDateTime getCreatedTime() {
//        return createdTime;
//    }
//
//    public void setCreatedTime(LocalDateTime createdTime) {
//        this.createdTime = createdTime;
//    }
}
