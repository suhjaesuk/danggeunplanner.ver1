package com.finalteam4.danggeunplanner.timer.entity;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class TimerTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TimerRepository timerRepository;


    @BeforeEach
    public void setUp(){
        String email = "test@gmail.com";
        String password = "test1234!";
        String username = "test";
        String profileImage= "test";
        Boolean isPlannerOpened = false;

        Member member = Member.builder()
                .email(email)
                .password(password)
                .username(username)
                .profileImage(profileImage)
                .isPlannerOpened(isPlannerOpened)
                .build();

        Member memberPersistence = memberRepository.save(member);

        LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        String content = "test";
        Integer continuousCount = 0;
        Boolean isFinish = false;

        Timer timer = Timer.builder()
                .member(memberPersistence)
                .startTime(startTime)
                .content(content)
                .continuousCount(continuousCount)
                .isFinish(isFinish)
                .build();

        timerRepository.save(timer);
    }

    @Test
    @Sql("classpath:db/tableinit.sql")
    public void startTimerTest(){

        //given
        LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        String content = "test";
        Integer continuousCount = 0;
        Boolean isFinish = false;

        String email = "test@gmail.com";
        String password = "test1234!";
        String username = "test";
        String profileImage= "test";
        Boolean isPlannerOpened = false;

        //when
        Optional<Timer> timerOptional = timerRepository.findById(1L);
        Timer timer = timerOptional.get();

        //then
        assertThat(startTime).isEqualTo(timer.getStartTime());
        assertThat(content).isEqualTo(timer.getContent());
        assertThat(continuousCount).isEqualTo(timer.getContinuousCount());
        assertThat(isFinish).isEqualTo(timer.getIsFinish());

        assertThat(email).isEqualTo(timer.getMember().getEmail());
        assertThat(password).isEqualTo(timer.getMember().getPassword());
        assertThat(username).isEqualTo(timer.getMember().getUsername());
        assertThat(profileImage).isEqualTo(timer.getMember().getProfileImage());
        assertThat(isPlannerOpened).isEqualTo(timer.getMember().getIsPlannerOpened());
    }

    @Test
    @Sql("classpath:db/tableinit.sql")
    public void finishTimerTest(){

        LocalDateTime endTime = LocalDateTime.of(2023, 1, 1, 0, 25, 0);
        Integer continuousCount = 1;
        Boolean isFinish = true;

        //when
        Optional<Timer> timerOptional = timerRepository.findById(1L);
        Timer timer = timerOptional.get();
        timer.finish(endTime,continuousCount);
        Optional<Timer> timerPersistenceOptional = timerRepository.findById(1L);
        Timer timerPersistence = timerPersistenceOptional.get();

        //then
        assertThat(timerPersistence.getEndTime()).isEqualTo(endTime);
        assertThat(timerPersistence.getContinuousCount()).isEqualTo(continuousCount);
        assertThat(timerPersistence.getIsFinish()).isEqualTo(isFinish);

    }

    @Test
    @Sql("classpath:db/tableinit.sql")
    public void updateTimerTest() {
        //given
        String content = "update content";

        //when
        Optional<Timer> timerOptional = timerRepository.findById(1L);
        Timer timer = timerOptional.get();
        timer.update(content);
        Optional<Timer> timerPersistenceOptional = timerRepository.findById(1L);
        Timer timerPersistence = timerPersistenceOptional.get();

        //then
        assertThat(timerPersistence.getContent()).isEqualTo(content);
    }
}