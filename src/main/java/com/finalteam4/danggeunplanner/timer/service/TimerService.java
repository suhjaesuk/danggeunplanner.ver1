package com.finalteam4.danggeunplanner.timer.service;


import com.finalteam4.danggeunplanner.common.util.TimeConverter;
import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.calendar.repository.CalendarRepository;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.service.MemberValidator;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.planner.repository.PlannerRepository;
import com.finalteam4.danggeunplanner.timer.dto.request.TimerFinishRequest;
import com.finalteam4.danggeunplanner.timer.dto.request.TimerStartRequest;
import com.finalteam4.danggeunplanner.timer.dto.request.TimerUpdateRequest;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerResponse;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerStartResponse;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_CALENDAR;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_PLANNER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_TIMER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimerService {
    private final TimerRepository timerRepository;
    private final PlannerRepository plannerRepository;
    private final CalendarRepository calendarRepository;
    private final MemberValidator memberValidator;

    @Transactional
    public TimerStartResponse startTimer(Member loggedInMember, TimerStartRequest request) {
        Timer timer = request.toTimer(loggedInMember);
        timerRepository.save(timer);
        return new TimerStartResponse(timer);
    }


    @Transactional
    public TimerResponse finishTimer(Member loggedInMember, Long timerId, TimerFinishRequest request) {
        Timer timer = timerRepository.findById(timerId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_TIMER)
        );

        memberValidator.validateAccess(loggedInMember, timer.getMember());

        timer.finish(request.getEndTime(), request.getCount()); //클라이언트와 count 를 continuousCount 로 바꾸자고 말해야함.
        deleteInactiveTimer(loggedInMember);

        createPlannerAndCalendarIfNotExists(loggedInMember, timer);

        Planner planner = plannerRepository.findByMemberAndDate(loggedInMember, TimeConverter.convertToPlannerDateForm(timer.getEndTime())).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_PLANNER)
        );
        Calendar calendar = calendarRepository.findByMemberAndDate(loggedInMember, TimeConverter.convertToCalendarDateForm(timer.getEndTime())).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_CALENDAR)
        );

        timer.confirmPlanner(planner);
        planner.confirmCalendar(calendar);

        planner.harvestCarrot();
        calendar.harvestCarrot();

        return new TimerResponse(timer);
    }
    @Transactional
    public TimerResponse updateTimer(Member loggedInMember, Long timerId, TimerUpdateRequest request) {
        Timer timer = timerRepository.findById(timerId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_TIMER)
        );
        memberValidator.validateAccess(loggedInMember, timer.getMember());

        timer.update(request.getContent());
        return new TimerResponse(timer);
    }

    private void createPlannerAndCalendarIfNotExists(Member member, Timer timer) {
        String plannerDate = TimeConverter.convertToPlannerDateForm(timer.getEndTime());
        String calendarDate = TimeConverter.convertToCalendarDateForm(timer.getEndTime());

        if (!plannerRepository.existsByMemberAndDate(member, plannerDate)) {
            Planner planner = new Planner(member, plannerDate);
            plannerRepository.save(planner);
        }

        if (!calendarRepository.existsByMemberAndDate(member, calendarDate)) {
            Calendar calendar = new Calendar(member, calendarDate);
            calendarRepository.save(calendar);
        }
    }


    private void deleteInactiveTimer(Member member) {
        List<Timer> timers = timerRepository.findAllByMember(member);

        for (Timer timer : timers) {
            if (timer.isRunning()) {
                timerRepository.delete(timer);
            }
        }
    }
}
