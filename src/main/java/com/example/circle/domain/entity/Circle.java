package com.example.circle.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Circle {
    @Id @GeneratedValue
    private Long id;

    private String circleName;

    @Embedded
    private MemberGrade limitMemberGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @OneToMany(mappedBy = "circle", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CircleMember> circleMembers = new ArrayList<>();

    @OneToMany(mappedBy = "circle", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CircleEvent> circleEvents = new ArrayList<>();

    public Circle(String circleName, Member owner, MemberGrade limitMemberGrade) {
        setCircleName(circleName);
        setOwner(owner);
        setLimitMemberGrade(limitMemberGrade);
    };

    public boolean isMemberSatisfiedGradeLimit(MemberGrade memberGrade) {
        if (this.limitMemberGrade.compareTo(memberGrade) <= 0) {
            return true;
        } else {
            return false;
        }
    }
}
