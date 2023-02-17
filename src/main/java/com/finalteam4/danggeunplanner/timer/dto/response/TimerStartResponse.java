package com.finalteam4.danggeunplanner.timer.dto.response;

import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TimerStartResponse {
    private final Long timerId;
    private final LocalDateTime startTime;

    public TimerStartResponse(Timer timer){
        this.timerId = timer.getId();
        this.startTime = timer.getStartTime();
    }

}
