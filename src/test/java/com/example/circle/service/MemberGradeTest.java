package com.example.circle.service;

import com.example.circle.domain.entity.MemberGrade;
import com.example.circle.domain.entity.MemberGradeDisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberGradeTest {
    @Test
    void 멤버_등급_비교_테스트() {
        MemberGrade grade1 = new MemberGrade(MemberGradeDisplayName.CHALLENGER);
        MemberGrade grade2 = new MemberGrade(MemberGradeDisplayName.BRONZE);
        MemberGrade grade3 = new MemberGrade(MemberGradeDisplayName.CHALLENGER);

        // grade1 은 grade2 보다 높으므로 양수가 나와야 한다.
        assertThat(grade1.compareTo(grade2)).isGreaterThan(0);
        // grade2 는 grade1 보다 낮으므로 음수가 나와야 한다.
        assertThat(grade2.compareTo(grade1)).isLessThan(0);
        // grade1 은 grade3 과 동일하므로 0이 나와야 한다.
        assertThat(grade1.compareTo(grade3)).isEqualTo(0);
    }
}
