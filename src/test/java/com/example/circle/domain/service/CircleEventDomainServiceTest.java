package com.example.circle.domain.service;

import com.example.circle.domain.dto.CircleEventJoinDto;
import com.example.circle.domain.dto.CreateCircleEventDto;
import com.example.circle.domain.entity.*;
import com.example.circle.domain.repository.CircleEventMemberRepository;
import com.example.circle.domain.repository.CircleEventRepository;
import com.example.circle.domain.repository.CircleRepository;
import com.example.circle.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class CircleEventDomainServiceTest {

    @Autowired
    CircleEventDomainService circleEventDomainService;

    @Autowired
    CircleRepository circleRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CircleEventRepository circleEventRepository;

    @Autowired
    CircleEventMemberRepository circleEventMemberRepository;

    Member owner;

    @BeforeEach
    void createMember() {
        Member newMember = new Member("모임장", new MemberGrade(MemberGradeDisplayName.BRONZE), "owner@naver.com");
        memberRepository.save(newMember);
        this.owner = newMember;
    }

    @Test
    void 서클_이벤트_생성_성공() {
        Circle circle = circleRepository.save(
                new Circle("서클1",
                        owner,
                        new MemberGrade(MemberGradeDisplayName.CHALLENGER)));
        CreateCircleEventDto input = new CreateCircleEventDto(
                circle.getId(),
                "모임타이틀",
                "모임설명",
                "모임장소",
                LocalDateTime.now(),
                2);
        Long createdCircleEventId = circleEventDomainService.createCircleEvent(input);
        CircleEvent created = circleEventRepository.findById(createdCircleEventId).get();
        assertThat(created.getTitle()).isEqualTo(input.getCircleEventTitle());
    }

    @Test
    void 서클_이벤트_생성_실패_존재하지_않는서클() {
        //
    }
    @Test
    void 서클_이벤트_참가_성공() {
        Circle circle = circleRepository.save(
                new Circle("서클1",
                        owner,
                        new MemberGrade(MemberGradeDisplayName.CHALLENGER)));
        Member member = memberRepository.save(
                new Member("멤버1",
                        new MemberGrade(MemberGradeDisplayName.BRONZE),
                        "bbb@naver.com"));
        CircleEvent circleEvent = circleEventRepository.save(
                new CircleEvent(
                        "모임명",
                        "모임타이틀",
                        "모임장소",
                        LocalDateTime.now(),
                        2,
                        circle
                )
        );

        CircleEventJoinDto input = new CircleEventJoinDto(member.getId(), circleEvent.getId());
        circleEventDomainService.joinCircleEvent(input);

        CircleEventMember eventMember = circleEventMemberRepository.findByCircleEvent(circleEvent);
        assertThat(eventMember.getMember().getId()).isEqualTo(member.getId());
    }


    @Test
    void 서클_이벤트_참가_실패_이미신청한이벤트() {
        Circle circle = circleRepository.save(
                new Circle("서클1",
                        owner,
                        new MemberGrade(MemberGradeDisplayName.CHALLENGER)));
        Member member = memberRepository.save(
                new Member("멤버1",
                        new MemberGrade(MemberGradeDisplayName.BRONZE),
                        "bbb@naver.com"));
        CircleEvent circleEvent = circleEventRepository.save(
                new CircleEvent(
                        "모임명",
                        "모임타이틀",
                        "모임장소",
                        LocalDateTime.now(),
                        2,
                        circle
                )
        );

        CircleEventJoinDto input = new CircleEventJoinDto(member.getId(), circleEvent.getId());
        circleEventDomainService.joinCircleEvent(input);
        assertThrows(IllegalStateException.class, () -> circleEventDomainService.joinCircleEvent(input));
    }

    @Test
    void 서클_이벤트_참가_실패_참가가능인원수초과() {
        Circle circle = circleRepository.save(
                new Circle("서클1",
                        owner,
                        new MemberGrade(MemberGradeDisplayName.CHALLENGER)));
        Member member1 = memberRepository.save(
                new Member("멤버1",
                        new MemberGrade(MemberGradeDisplayName.BRONZE),
                        "bbb@naver.com"));
        Member member2 = memberRepository.save(
                new Member("멤버2",
                        new MemberGrade(MemberGradeDisplayName.BRONZE),
                        "ccc@naver.com"));
        CircleEvent circleEvent = circleEventRepository.save(
                new CircleEvent(
                        "모임명",
                        "모임타이틀",
                        "모임장소",
                        LocalDateTime.now(),
                        1,
                        circle
                )
        );

        CircleEventJoinDto input1 = new CircleEventJoinDto(member1.getId(), circleEvent.getId());
        circleEventDomainService.joinCircleEvent(input1);
        CircleEventJoinDto input2 = new CircleEventJoinDto(member2.getId(), circleEvent.getId());
        assertThrows(IllegalStateException.class, () -> circleEventDomainService.joinCircleEvent(input2));
    }
}
