package com.finalteam4.danggeunplanner.planner.entity;

import com.finalteam4.danggeunplanner.common.util.TimeConverter;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="plan_id")
    private Long id;
    @Column(name="start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name="end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String date;
    private String content;

    private Boolean isFinished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="planner_id")
    private Planner planner;

    @Builder
    public Plan(String date, LocalDateTime startTime, LocalDateTime endTime, String content, Boolean isFinished, Member member){
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
        this.isFinished = isFinished;
        this.member = member;
    }

    public void confirmPlanner(Planner planner){
        this.planner = planner;
        planner.addPlan(this);
    }


    public void updateContent(LocalDateTime startTime, LocalDateTime endTime, String content){
        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
    }

    public boolean isStartTimeAndEndTimeSameDate(){
        return !TimeConverter.convertToPlannerDateForm(startTime).equals(TimeConverter.convertToPlannerDateForm(endTime));
    }

    public boolean isEndTimeLessThanStartTime(){
        return endTime.isBefore(startTime) ||
                endTime.isEqual(startTime);
    }

    public void updateDoneStatus(){
        this.isFinished = !isFinished;
    }

    public boolean isFinished(){
        return isFinished.equals(true);
    }

    public boolean isProgressed(){
        return isFinished.equals(false);
    }
}
