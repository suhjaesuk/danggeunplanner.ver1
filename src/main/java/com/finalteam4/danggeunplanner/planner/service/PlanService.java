package com.finalteam4.danggeunplanner.planner.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.common.util.TimeConverter;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.service.MemberValidator;
import com.finalteam4.danggeunplanner.planner.dto.request.PlanRequest;
import com.finalteam4.danggeunplanner.planner.dto.response.PlanResponse;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.planner.repository.PlanRepository;
import com.finalteam4.danggeunplanner.planner.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.DIFFERENT_PLANNING_DATE;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_PLAN;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_PLANNER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_PLANNING_TIME;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
    private final PlannerRepository plannerRepository;
    private final MemberValidator memberValidator;

    @Transactional
    public PlanResponse createPlan(Member loggedInMember, PlanRequest request) {
        Plan plan = request.toPlan(loggedInMember);

        validateDate(plan);
        validateTime(plan);

        planRepository.save(plan);

        validateExistByMemberAndDate(loggedInMember, plan);
        createPlanner(loggedInMember, plan);

        Planner planner = findPlannerForMemberAndDate(loggedInMember, plan.getDate());
        plan.confirmPlanner(planner);

        return new PlanResponse(plan);
    }

    @Transactional
    public PlanResponse updatePlanContent(Member loggedInMember, Long planId, PlanRequest request) {
        Plan plan = findPlanById(planId);
        memberValidator.validateAccess(loggedInMember, plan.getMember());

        plan.updateContent(request.getStartTime(), request.getEndTime(), request.getContent());

        validateTime(plan);
        validateDate(plan);

        return new PlanResponse(plan);
    }

    @Transactional
    public PlanResponse deletePlan(Member loggedInMember, Long planId) {
        Plan plan = findPlanById(planId);
        memberValidator.validateAccess(loggedInMember, plan.getMember());

        planRepository.delete(plan);
        return new PlanResponse(plan);
    }
    @Transactional
    public PlanResponse updatePlanDoneStatus(Member loggedInMember, Long planId) {
        Plan plan = findPlanById(planId);
        memberValidator.validateAccess(loggedInMember, plan.getMember());

        plan.updateDoneStatus();
        return new PlanResponse(plan);
    }

    private void validateExistByMemberAndDate(Member member, Plan plan){
        if(!plannerRepository.existsByMemberAndDate(member, plan.getDate())){
            throw new DanggeunPlannerException(NOT_FOUND_PLANNER);
        }
    }
    private void createPlanner(Member member, Plan plan) {
        Planner planner = new Planner(member, plan.getDate());
        plannerRepository.save(planner);
    }

    private Plan findPlanById(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new DanggeunPlannerException(NOT_FOUND_PLAN));
    }

    private Planner findPlannerForMemberAndDate(Member member, String date) {
        return plannerRepository.findByMemberAndDate(member, date)
                .orElseThrow(() -> new DanggeunPlannerException(NOT_FOUND_PLANNER));
    }

    public void validateDate(Plan plan) {
        if(plan.isStartTimeAndEndTimeSameDate()){
            throw new DanggeunPlannerException(DIFFERENT_PLANNING_DATE);
        }
    }

    public void validateTime(Plan plan) {
        if (plan.isEndTimeLessThanStartTime()) {
            throw new DanggeunPlannerException(NOT_VALID_PLANNING_TIME);
        }
    }

}
