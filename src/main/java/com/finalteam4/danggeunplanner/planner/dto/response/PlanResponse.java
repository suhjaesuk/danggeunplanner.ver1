package com.finalteam4.danggeunplanner.planner.dto.response;

import com.finalteam4.danggeunplanner.common.util.TimeConverter;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import lombok.Getter;

@Getter
public class PlanResponse {

    private final Long planId;
    private final String startTime;
    private final String endTime;
    private final String content;
    private final Boolean isFinished;

    public PlanResponse(Plan plan){
        this.planId = plan.getId();
        this.startTime = TimeConverter.convertToHourMinute(plan.getStartTime());
        this.endTime = TimeConverter.convertToHourMinute(plan.getEndTime());
        this.content = plan.getContent();
        this.isFinished = plan.getIsFinished();
    }
}
