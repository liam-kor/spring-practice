package com.example.circle.domain.entity;

import lombok.*;
import org.apache.commons.validator.routines.EmailValidator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String userName;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<CircleMember> joinedCircles = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<CircleEventMember> joinedCircleEvents = new ArrayList<>();

    @Embedded
    private MemberGrade grade; // 멤버 등급

    public Member(String userName, MemberGrade grade, String email) {
        setUserName(userName);
        setGrade(grade);
        setEmail(email);
    }

    public void update(String userName, MemberGrade memberGrade, String email) {
        setUserName(userName);
        setGrade(memberGrade);
        setEmail(email);
    }

    private void setUserName(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("유저명은 필수로 입력되어야 합니다.");
        }
        this.userName = userName;
    }

    private void setEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException("잘못된 이메일 형식입니다.");
        }
        this.email = email;
    }

    private void setGrade(MemberGrade grade) {
        if (grade == null) {
            throw new IllegalArgumentException("유저 등급은 필수로 입력되어야 합니다.");
        }
        this.grade = grade;
    }
}