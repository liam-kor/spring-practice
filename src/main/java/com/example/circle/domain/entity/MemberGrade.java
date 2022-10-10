package com.example.circle.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
@NoArgsConstructor
@Getter
public class MemberGrade implements Comparable<MemberGrade>{
    private MemberGradeDisplayName displayName;

    public MemberGrade(MemberGradeDisplayName memberGradeDisplayName) {
        this.displayName = memberGradeDisplayName;
    }

    public int compareTo(MemberGrade memberGrade) {
        return memberGrade.getDisplayName().ordinal() - this.displayName.ordinal();
    }

    public static MemberGrade fromDisplayName(MemberGradeDisplayName displayName) {
        return new MemberGrade(displayName);
    }
}
