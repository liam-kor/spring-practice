package com.example.circle.domain.service;

import com.example.circle.domain.dto.CircleEventJoinDto;
import com.example.circle.domain.dto.CreateCircleEventDto;
import com.example.circle.domain.entity.Circle;
import com.example.circle.domain.entity.CircleEvent;
import com.example.circle.domain.entity.CircleEventMember;
import com.example.circle.domain.entity.Member;
import com.example.circle.domain.repository.CircleEventMemberRepository;
import com.example.circle.domain.repository.CircleEventRepository;
import com.example.circle.domain.repository.CircleRepository;
import com.example.circle.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CircleEventDomainService {
    private final CircleRepository circleRepository;
    private final MemberRepository memberRepository;

    private final CircleEventRepository circleEventRepository;

    private final CircleEventMemberRepository circleEventMemberRepository;


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
