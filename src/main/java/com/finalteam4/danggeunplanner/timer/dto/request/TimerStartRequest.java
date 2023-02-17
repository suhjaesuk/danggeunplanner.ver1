package com.finalteam4.danggeunplanner.timer.dto.request;

import com.finalteam4.danggeunplanner.common.exception.ValidationGroups;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class TimerStartRequest {
    @NotNull(message = "시작 시간을 입력해주세요.", groups = ValidationGroups.FirstNotNullGroup.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    public Timer toTimer(Member member){
        return Timer.builder()
                .member(member)
                .startTime(startTime)
                .content("당근 수확!\uD83D\uDE0A")
                .continuousCount(0)
                .isFinish(false)
                .build();
    }

    //테스트 코드를 위한 set
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
