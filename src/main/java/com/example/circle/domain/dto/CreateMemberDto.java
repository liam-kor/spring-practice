package com.example.circle.domain.dto;

import com.example.circle.domain.entity.MemberGradeDisplayName;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class CreateMemberDto {
    private String memberName;
    private MemberGradeDisplayName memberGradeDisplayName;
    private String email;

    public CreateMemberDto(String memberName, MemberGradeDisplayName memberGradeDisplayName, String email) {
        this.memberName = memberName;
        this.memberGradeDisplayName = memberGradeDisplayName;
        this.email = email;
    }
}
