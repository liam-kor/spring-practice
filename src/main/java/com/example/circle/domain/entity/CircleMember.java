package com.example.circle.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class CircleMember {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "circle_id")
    private Circle circle;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public CircleMember(Circle circle, Member member) {
        this.circle = circle;
        this.member = member;
    }
}
