package com.finalteam4.danggeunplanner.member.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String username;

    @Column(name="profile_image",nullable = false)
    private String profileImage;

    public Member(String email, String password){
        this.email = email;
        this.password = password;
        this.profileImage = "https://item.kakaocdn.net/do/1e917e59f980468a78f2bff7dcc25ac215b3f4e3c2033bfd702a321ec6eda72c";
    }

    public void updateUsername(String username){
        this.username = username;
    }
}
