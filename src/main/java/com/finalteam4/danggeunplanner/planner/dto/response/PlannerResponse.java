package com.finalteam4.danggeunplanner.planner.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlannerResponse {
    private final Boolean isOwner;
    private final Boolean isPlannerOpened;
    private final String username;
    private final String profileImage;
    private final Integer carrot;
    private final List<Object> contents = new ArrayList<>();

    //검색한 유저의 플래너가 DB에 없을 때 응답
    public PlannerResponse(Member searchedMember, Member loggedInMember) {
        this.isOwner = searchedMember.getUsername().equals(loggedInMember.getUsername());
        this.isPlannerOpened = searchedMember.isPlannerOpened();
        this.username = searchedMember.getUsername();
        this.profileImage = searchedMember.getProfileImage();
        this.carrot = 0;
    }

    //검색한 유저의 플래너가 DB에 있을 때 응답
    public PlannerResponse(Planner planner, Member loggedInMember) {
        this.isOwner = planner.getMember().getUsername().equals(loggedInMember.getUsername());
        this.isPlannerOpened = planner.getMember().isPlannerOpened();
        this.username = planner.getMember().getUsername();
        this.profileImage = planner.getMember().getProfileImage();
        this.carrot = planner.getCarrot();
    }

    public void addPlan(PlanResponse response) {
        contents.add(response);
    }

    public void addTimer(TimerResponse response) {
        contents.add(response);
    }
}
