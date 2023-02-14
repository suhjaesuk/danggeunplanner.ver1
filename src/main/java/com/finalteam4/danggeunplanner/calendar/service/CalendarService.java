package com.finalteam4.danggeunplanner.calendar.service;

import com.finalteam4.danggeunplanner.TimeConverter;
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

        if (calendarOptional.isPresent()) {
            Calendar calendar = calendarOptional.get();
            CalendarResponse response = new CalendarResponse(calendar);
            updateColorStage(calendar, response);
            return response;
        }

        return new CalendarResponse(member);
    }

    private void updateColorStage(Calendar calendar, CalendarResponse response) {
        ColorStageResponse colorStage = new ColorStageResponse();
        calendar.getPlanners().forEach(planner -> {
            int carrot = planner.getCarrot();
            String date = planner.getDate();

            if (date.equals(TimeConverter.convertToPlannerDateForm(LocalDateTime.now()))) {
                //todayColorStage는 CalendarResponse에 int로 존재한다.
                response.updateTodayColorStage(carrot);
            } else {
                //오늘날짜를 제외한 ColorStage는 CalendarResponse에 List<ColorStage>로 존재한다.
                colorStage.addDateToColorStage(date, carrot);
            }
        });
        response.addColorStage(colorStage);
    }

}



