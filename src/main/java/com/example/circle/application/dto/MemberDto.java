package com.example.circle.application.dto;

import com.example.circle.domain.entity.CircleMember;
import com.example.circle.domain.entity.Member;
import com.example.circle.domain.entity.MemberGrade;
import com.example.circle.domain.entity.MemberGradeDisplayName;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
@Getter
public class MemberDto {
    private String userName;
    private MemberGrade grade;
    private String email;
}
