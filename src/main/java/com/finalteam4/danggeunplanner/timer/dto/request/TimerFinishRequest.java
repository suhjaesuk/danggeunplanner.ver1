package com.finalteam4.danggeunplanner.timer.dto.request;

import com.finalteam4.danggeunplanner.common.exception.ValidationGroups;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class TimerFinishRequest {

    @NotNull(message = "종료 시간을 입력해주세요.", groups = ValidationGroups.FirstNotNullGroup.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @NotNull(message = "연속 횟수를 알려주세요.", groups = ValidationGroups.SecondNotNullGroup.class)
    private Integer count; //클라이언트와 continuousCount로 바꾸자고 말맞춰야함.

    //TimerService 테스트를 위한 Set
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
