package com.finalteam4.danggeunplanner.planner.entity;

import com.finalteam4.danggeunplanner.common.util.TimeConverter;
import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.AccessLevel;
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
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="planner_id")
    private Long id;
    @Column(nullable = false)
    private String date;
    @Column(nullable = false)
    private Integer carrot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="calendar_id")
    private Calendar calendar;

    @OneToMany(mappedBy ="planner")
    private List<Plan> plans = new ArrayList<>();
    @OneToMany(mappedBy ="planner")
    private List<Timer> timers = new ArrayList<>();

    public void addPlan(Plan plan){
        plans.add(plan);
    }

    public void addTimer(Timer timer){
        timers.add(timer);
    }

    public void confirmCalendar(Calendar calendar){
        this.calendar=calendar;
        calendar.addPlanner(this);
    }

    @Builder
    public Planner(Member member, String date){
        this.date = date;
        this.member = member;
        this.carrot=0;
    }

    public void harvestCarrot(){
        this.carrot++;
    }

    public boolean isSameDate(LocalDateTime localDateTime){
        return date.equals(TimeConverter.convertToPlannerDateForm(localDateTime));
    }

    public boolean isDifferentDate(LocalDateTime localDateTime){
        return !date.equals(TimeConverter.convertToPlannerDateForm(localDateTime));
    }
}