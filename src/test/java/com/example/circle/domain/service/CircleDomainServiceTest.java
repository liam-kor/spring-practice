package com.example.circle.domain.service;


import com.example.circle.domain.dto.CircleEventJoinDto;
import com.example.circle.domain.dto.CircleMemberJoinDto;
import com.example.circle.domain.dto.CreateCircleDto;
import com.example.circle.domain.dto.CreateCircleEventDto;
import com.example.circle.domain.entity.*;
import com.example.circle.domain.repository.*;
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
public class CircleDomainServiceTest {
    @Autowired
    CircleDomainService circleDomainService;

    @Autowired
    CircleRepository circleRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CircleMemberRepository circleMemberRepository;

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
    void 서클_생성_테스트_성공() {
        // when
        CreateCircleDto createCircleDto = new CreateCircleDto("서클1", owner.getId(), MemberGradeDisplayName.BRONZE);
        Long createdCircleId = circleDomainService.createCircle(createCircleDto);

        // then
        Circle createdCircle = circleRepository.findById(createdCircleId).get();
        assertThat(createdCircle.getCircleName()).isEqualTo("서클1");
    }

    @Test
    void 서클_생성_테스트_실패_존재하지않는유저아이디() {
        // given
        Long nonExistsMemberId = owner.getId() + 1;
        // when
        CreateCircleDto createCircleDto = new CreateCircleDto("서클1", nonExistsMemberId, MemberGradeDisplayName.BRONZE);
        // then
        assertThrows(IllegalStateException.class, () -> {
            circleDomainService.createCircle(createCircleDto);
        });
    }

    @Test
    void 서클_생성_테스트_실패_이미존재하는서클명() {
        // given
        CreateCircleDto createCircleDto = new CreateCircleDto("서클1", owner.getId(), MemberGradeDisplayName.BRONZE);
        circleDomainService.createCircle(createCircleDto);

        // when & then
        assertThrows(IllegalStateException.class, () -> {
            circleDomainService.createCircle(createCircleDto);
        });
    }

    @Test
    void 서클_가입_테스트_성공() {
        // given
        Circle circle = circleRepository.save(
                new Circle("서클1",
                        owner,
                        new MemberGrade(MemberGradeDisplayName.BRONZE)));

        Member member = memberRepository.save(
                new Member("멤버1",
                        new MemberGrade(MemberGradeDisplayName.CHALLENGER),
                        "bbb@naver.com"));

        CircleMemberJoinDto input = new CircleMemberJoinDto(circle.getId(), member.getId());
        circleDomainService.joinMember(input);

        // when
        CircleMember circleMember = circleMemberRepository.findByCircle(circle);

        // then
        assertThat(circleMember.getMember().getEmail()).isEqualTo(member.getEmail());

    }

    @Test
    void 서클_가입_테스트_실패_이미가입된서클() {
        // given
        Circle circle = circleRepository.save(
                new Circle("서클1",
                        owner,
                        new MemberGrade(MemberGradeDisplayName.BRONZE)));

        Member member = memberRepository.save(
                new Member("멤버1",
                        new MemberGrade(MemberGradeDisplayName.CHALLENGER),
                        "bbb@naver.com"));

        CircleMemberJoinDto input = new CircleMemberJoinDto(circle.getId(), member.getId());
        circleDomainService.joinMember(input);

        // when & then
        assertThrows(IllegalStateException.class, () -> circleDomainService.joinMember(input));
    }

    @Test
    void 서클_가입_테스트_실패_가입제한등급미달() {
        // given
        Circle circle = circleRepository.save(
                new Circle("서클1",
                        owner,
                        new MemberGrade(MemberGradeDisplayName.CHALLENGER)));

        Member member = memberRepository.save(
                new Member("멤버1",
                        new MemberGrade(MemberGradeDisplayName.BRONZE),
                        "bbb@naver.com"));

        CircleMemberJoinDto input = new CircleMemberJoinDto(circle.getId(), member.getId());

        // when & then
        assertThrows(IllegalStateException.class, () -> circleDomainService.joinMember(input));
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
        Long createdCircleEventId = circleDomainService.createCircleEvent(input);
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
        circleDomainService.joinCircleEvent(input);

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
        circleDomainService.joinCircleEvent(input);
        assertThrows(IllegalStateException.class, () -> circleDomainService.joinCircleEvent(input));
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
        circleDomainService.joinCircleEvent(input1);
        CircleEventJoinDto input2 = new CircleEventJoinDto(member2.getId(), circleEvent.getId());
        assertThrows(IllegalStateException.class, () -> circleDomainService.joinCircleEvent(input2));
    }

}