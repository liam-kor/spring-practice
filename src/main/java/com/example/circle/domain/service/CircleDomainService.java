package com.example.circle.domain.service;

import com.example.circle.domain.dto.CircleEventJoinDto;
import com.example.circle.domain.dto.CircleMemberJoinDto;
import com.example.circle.domain.dto.CreateCircleDto;
import com.example.circle.domain.dto.CreateCircleEventDto;
import com.example.circle.domain.entity.*;
import com.example.circle.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CircleDomainService {
    private final CircleRepository circleRepository;
    private final MemberRepository memberRepository;

    private final CircleMemberRepository circleMemberRepository;

    private final CircleEventRepository circleEventRepository;

    private final CircleEventMemberRepository circleEventMemberRepository;

    @Transactional
    public void joinMember(CircleMemberJoinDto input) {
        Circle circle = circleRepository.findById(input.getCircleId()).orElseThrow(() -> new IllegalStateException("존재하지 않는 서클입니다."));
        Member member = memberRepository.findById(input.getMemberId()).orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        CircleMember isAlreadyJoined = circleMemberRepository.findByCircle(circle);
        if (isAlreadyJoined != null) {
            throw new IllegalStateException("이미 가입된 서클입니다.");
        }
        if (!circle.isMemberSatisfiedGradeLimit(member.getGrade())) {
            throw new IllegalStateException("가입 등급이 맞지 않습니다.");
        } else {
            System.out.println("가입 등급이 적절합니다.");
        }
        CircleMember circleMember = new CircleMember(circle, member);
        circleMemberRepository.save(circleMember);
    }

    @Transactional
    public Long createCircle(CreateCircleDto input) {
        Member owner = memberRepository.findById(input.getOwnerId()).orElseThrow(() -> new IllegalStateException("존재하지 않는 유저 아이디"));
        Circle isExists = circleRepository.findByCircleName(input.getCircleName());
        if (isExists != null) {
            throw new IllegalStateException("이미 존재하는 서클명입니다.");
        }
        Circle circle = new Circle(input.getCircleName(), owner, new MemberGrade(input.getGradeLimit()));
        Circle createdCircle = circleRepository.save(circle);
        return createdCircle.getId();
    }

    @Transactional
    public Long createCircleEvent(CreateCircleEventDto input) {
        Circle circle = circleRepository.findById(input.getCircleId()).orElseThrow(() -> new IllegalStateException("존재하지 않는 서클입니다."));
        CircleEvent circleEvent = new CircleEvent(
                input.getCircleEventTitle(),
                input.getCircleEventDescription(),
                input.getCircleEventLocation(),
                input.getStartedAt(),
                input.getMaxJoinMemberLimit(),
                circle);
        CircleEvent createdCircleEvent = circleEventRepository.save(circleEvent);
        return createdCircleEvent.getId();
    }

    @Transactional
    public void joinCircleEvent(CircleEventJoinDto input) {
        CircleEvent circleEvent = circleEventRepository.findById(input.getCircleEventId()).orElseThrow(() -> new IllegalStateException("존재하지 않는 서클이벤트 입니다."));
        Member member = memberRepository.findById(input.getMemberId()).orElseThrow(() -> new IllegalStateException("존재하지 않는 멤버입니다."));
        Long memberCount = circleEventMemberRepository.countByCircleEvent(circleEvent);
        if (memberCount >= circleEvent.getMaxJoinMemberLimit()) {
            throw new IllegalStateException("모임의 정원이 초과되어 신청하실 수 없습니다.");
        }
        CircleEventMember alreadyJoined = circleEventMemberRepository.findByCircleEventAndMember(circleEvent, member);
        if (alreadyJoined != null) {
            throw new IllegalStateException("이미 신청한 모임입니다.");
        }
        CircleEventMember circleEventMember = new CircleEventMember(circleEvent, member);
        circleEventMemberRepository.save(circleEventMember);
    }
}
