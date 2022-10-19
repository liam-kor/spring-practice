package com.example.circle.domain.repository;

import com.example.circle.domain.entity.CircleEvent;
import com.example.circle.domain.entity.CircleEventMember;
import com.example.circle.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircleEventMemberRepository extends JpaRepository<CircleEventMember, Long> {
    CircleEventMember findByCircleEvent(CircleEvent circleEvent);

    CircleEventMember findByCircleEventAndMember(CircleEvent circleEvent, Member member);

    Long countByCircleEvent(CircleEvent circleEvent);
}
