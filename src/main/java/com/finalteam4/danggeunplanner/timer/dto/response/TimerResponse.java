package com.finalteam4.danggeunplanner.timer.dto.response;

import com.finalteam4.danggeunplanner.common.util.TimeConverter;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.Getter;

@Getter
public class TimerResponse {
    private final Long timerId;
    private final String startTime;
    private final String endTime;
    private final String content;
    private final Integer count; //클라이언트와 count 를 continuousCount 로 바꾸자 말해야함.

    public TimerResponse(Timer timer){
        this.timerId = timer.getId();
        this.startTime = TimeConverter.convertToHourMinute(timer.getStartTime());
        this.endTime = TimeConverter.convertToHourMinute(timer.getEndTime());
        this.content = timer.getContent();
        this.count = timer.getContinuousCount();
    }
}
