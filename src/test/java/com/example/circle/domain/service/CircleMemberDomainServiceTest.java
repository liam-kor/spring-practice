package com.example.circle.domain.service;

import com.example.circle.domain.dto.CircleMemberJoinDto;
import com.example.circle.domain.entity.*;
import com.example.circle.domain.repository.CircleMemberRepository;
import com.example.circle.domain.repository.CircleRepository;
import com.example.circle.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class CircleMemberDomainServiceTest {

    @Autowired
    CircleRepository circleRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CircleMemberDomainService circleMemberDomainService;

    @Autowired
    CircleMemberRepository circleMemberRepository;

    Member owner;

    @BeforeEach
    void createMember() {
        Member newMember = new Member("모임장", new MemberGrade(MemberGradeDisplayName.BRONZE), "owner@naver.com");
        memberRepository.save(newMember);
        this.owner = newMember;
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
        circleMemberDomainService.joinMember(input);

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
        circleMemberDomainService.joinMember(input);

        // when & then
        assertThrows(IllegalStateException.class, () -> circleMemberDomainService.joinMember(input));
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
        assertThrows(IllegalStateException.class, () -> circleMemberDomainService.joinMember(input));
    }
}
