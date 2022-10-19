package com.example.circle.domain.dto;

import com.example.circle.domain.entity.MemberGrade;
import com.example.circle.domain.entity.MemberGradeDisplayName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateMemberDto {
    private String userName;
    private MemberGradeDisplayName gradeDisplayName;
    private String email;
}
