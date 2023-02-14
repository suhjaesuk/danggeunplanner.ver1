package com.finalteam4.danggeunplanner.calendar.service;

import com.finalteam4.danggeunplanner.calendar.dto.response.CalendarResponse;
import com.finalteam4.danggeunplanner.calendar.dto.response.ColorStageResponse;
import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.calendar.repository.CalendarRepository;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final MemberRepository memberRepository;

    public CalendarResponse findCalendar(String username, String date) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new DanggeunPlannerException(NOT_FOUND_MEMBER));

        Optional<Calendar> calendarOptional = calendarRepository.findByMemberAndDate(member, date);

        CalendarResponse response = calendarOptional.map(CalendarResponse::new)
                .orElseGet(() -> new CalendarResponse(member));

        updateColorStage(calendarOptional.map(Calendar::getPlanners).orElse(Collections.emptyList()), response);

        return response;
    }

    private void updateColorStage(List<Planner> planners, CalendarResponse response) {
        ColorStageResponse colorStage = new ColorStageResponse();

        planners.stream()
                .filter(planner -> planner.isDifferentDate(LocalDateTime.now()))
                .forEach(planner -> colorStage.updateColorStage(planner.getDate(), planner.getCarrot()));

        response.addColorStage(colorStage);

        planners.stream()
                .filter(planner -> planner.isSameDate(LocalDateTime.now()))
                .findFirst()
                .ifPresent(planner -> response.updateTodayColorStage(planner.getCarrot()));
    }
}