package com.example.circle.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "circle_event_member")
public class CircleEventMember {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "circle_event_id")
    private CircleEvent circleEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public CircleEventMember(CircleEvent circleEvent, Member member) {
        this.circleEvent = circleEvent;
        this.member = member;
    }
}