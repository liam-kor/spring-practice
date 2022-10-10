package com.example.circle.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Circle {
    @Id @GeneratedValue
    private Long id;

    private String circleName;

    @Embedded
    private MemberGrade limitMemberGrade;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Member owner;

    @OneToMany(mappedBy = "circle", orphanRemoval = true)
    private List<CircleMember> circleMembers = new ArrayList<>();

    public Circle(String circleName, Member owner, MemberGrade limitMemberGrade) {
        this.circleName = circleName;
        this.owner = owner;
        this.limitMemberGrade = limitMemberGrade;
    };

    public boolean isMemberSatisfiedGradeLimit(MemberGrade memberGrade) {
        if (this.limitMemberGrade.compareTo(memberGrade) <= 0) {
            return true;
        } else {
            return false;
        }
    }
}
