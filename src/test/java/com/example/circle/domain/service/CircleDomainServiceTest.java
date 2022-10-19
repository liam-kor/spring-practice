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

}