package com.finalteam4.danggeunplanner.planner.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.planner.dto.response.PlanResponse;
import com.finalteam4.danggeunplanner.planner.dto.response.PlannerResponse;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.planner.repository.PlannerRepository;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerResponse;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlannerService {
    private final PlannerRepository plannerRepository;
    private final MemberRepository memberRepository;

    public PlannerResponse getPlanner(Member loggedInMember, String searchedUsername, String date) {
        return getPlannerResponse(loggedInMember, searchedUsername, date, true, true, true);
    }

    public PlannerResponse getPlan(Member loggedInMember, String searchedUsername, String date) {
        return getPlannerResponse(loggedInMember, searchedUsername, date, true, true, false);
    }

    public PlannerResponse getFinishedPlan(Member loggedInMember, String searchedUsername, String date) {
        return getPlannerResponse(loggedInMember, searchedUsername, date, false, true, false);
    }

    public PlannerResponse getTimer(Member loggedInMember, String searchedUsername, String date) {
        return getPlannerResponse(loggedInMember, searchedUsername, date, false, false, true);
    }

    private PlannerResponse getPlannerResponse(Member loggedInMember, String searchedUsername, String date, boolean includeProgressedPlans,  boolean includeFinishedPlans, boolean includeTimers) {
        Member searchedMember = memberRepository.findByUsername(searchedUsername)
                .orElseThrow(() -> new DanggeunPlannerException(NOT_FOUND_MEMBER));

        return plannerRepository.findByMemberAndDate(searchedMember, date)
                .map(planner -> {
                    PlannerResponse response = new PlannerResponse(planner, loggedInMember);
                    if (includeProgressedPlans) {
                        for (Plan plan : planner.getPlans()) {
                            if(plan.isProgressed()) response.addPlan(new PlanResponse(plan));
                        }
                    }
                    if (includeFinishedPlans) {
                        for (Plan plan : planner.getPlans()) {
                            if(plan.isFinished()) response.addPlan(new PlanResponse(plan));

                        }
                    }
                    if (includeTimers) {
                        for (Timer timer : planner.getTimers()) {
                            response.addTimer(new TimerResponse(timer));
                        }
                    }
                    return response;
                })
                .orElse(new PlannerResponse(searchedMember, loggedInMember));
    }
}
