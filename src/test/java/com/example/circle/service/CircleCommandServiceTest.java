package com.example.circle.service;

import com.example.circle.application.dto.CircleMemberJoinDto;
import com.example.circle.application.dto.CreateCircleDto;
import com.example.circle.application.service.CircleCommandService;
import com.example.circle.domain.entity.*;
import com.example.circle.domain.repository.CircleMemberRepository;
import com.example.circle.domain.repository.CircleRepository;
import com.example.circle.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class CircleCommandServiceTest {
    @Autowired
    CircleCommandService circleCommandService;

    @Autowired
    CircleRepository circleRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CircleMemberRepository circleMemberRepository;

    Member owner;

    @BeforeEach
    void createMember() {
        Member newMember = new Member("모임장", new MemberGrade(MemberGradeDisplayName.BRONZE), "owner@naver.com");
        memberRepository.save(newMember);
        this.owner = newMember;
        System.out.println(newMember.getId());
    }

    @Test
    void 서클_생성_테스트_성공() {
        // when
        CreateCircleDto createCircleDto = new CreateCircleDto("서클1", owner.getId(), MemberGradeDisplayName.BRONZE);
        Long createdCircleId = circleCommandService.createCircle(createCircleDto);

        // then
        Circle createdCircle = circleRepository.findById(createdCircleId).get();
        assertThat(createdCircle.getCircleName()).isEqualTo("서클1");
    }

    @Test
    void 서클_생성_테스트_실패_존재하지않는유저아이디() {
        Long nonExistsMemberId = owner.getId() + 1;
        // when
        CreateCircleDto createCircleDto = new CreateCircleDto("서클1", nonExistsMemberId, MemberGradeDisplayName.BRONZE);

        assertThrows(IllegalStateException.class, () -> {
            circleCommandService.createCircle(createCircleDto);
        });
    }

    @Test
    void 서클_생성_테스트_실패_이미존재하는서클명() {
        CreateCircleDto createCircleDto = new CreateCircleDto("서클1", owner.getId(), MemberGradeDisplayName.BRONZE);
        circleCommandService.createCircle(createCircleDto);

        assertThrows(IllegalStateException.class, () -> {
            circleCommandService.createCircle(createCircleDto);
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
        circleCommandService.joinMember(input);

        CircleMember circleMember = circleMemberRepository.findByCircle(circle);
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
        circleCommandService.joinMember(input);
        assertThrows(IllegalStateException.class, () -> circleCommandService.joinMember(input));
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
        assertThrows(IllegalStateException.class, () -> circleCommandService.joinMember(input));
    }
}
