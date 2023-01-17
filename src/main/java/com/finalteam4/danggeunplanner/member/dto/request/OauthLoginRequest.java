package com.finalteam4.danggeunplanner.member.dto.request;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OauthLoginRequest {
    private String email;

    @Builder
    public OauthLoginRequest(String email){
        this.email = email;
    }

    public Member toEntity(String email){
        return Member.builder()
                .email(email)
                .profileImage("https://danggeunplanner-bucket.s3.ap-northeast-2.amazonaws.com/images/77e5ffbb-37aa-499b-b79d-9e403c43268dprofile_pic.png")
                .build();
    }
}
