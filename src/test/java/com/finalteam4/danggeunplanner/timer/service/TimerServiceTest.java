package com.finalteam4.danggeunplanner.timer.service;

import com.finalteam4.danggeunplanner.calendar.repository.CalendarRepository;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.service.MemberValidator;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.planner.repository.PlannerRepository;
import com.finalteam4.danggeunplanner.timer.dto.request.TimerFinishRequest;
import com.finalteam4.danggeunplanner.timer.dto.request.TimerStartRequest;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerResponse;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerStartResponse;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TimerServiceTest {

    @Mock
    private TimerRepository timerRepository;
    @Mock
    private PlannerRepository plannerRepository;
    @Mock
    private CalendarRepository calendarRepository;
    @Mock
    private MemberValidator memberValidator;

    @InjectMocks
    private TimerService timerService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void startTimerTest() {

        //given
        Member member = Member.builder()
                .email("test@gmail.com")
                .password("test1234!")
                .username("test")
                .profileImage("test")
                .isPlannerOpened(false)
                .build();

        TimerStartRequest request = new TimerStartRequest();
        request.setStartTime(LocalDateTime.of(2023, 1, 1, 0, 0, 0));
        Timer timer = request.toTimer(member);

        //stub
        when(timerRepository.save(any())).thenReturn(timer);

        // when
        TimerStartResponse response = timerService.startTimer(member, request);

        // then
        assertThat(response.getStartTime()).isEqualTo(LocalDateTime.of(2023, 1, 1, 0, 0, 0));
    }

    @Test
    public void finishTimerTest() {
    }
}