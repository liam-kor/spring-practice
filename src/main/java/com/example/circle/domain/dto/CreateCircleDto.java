package com.example.circle.domain.dto;

import com.example.circle.domain.entity.MemberGradeDisplayName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateCircleDto {
    private String circleName;
    private Long ownerId;
    private MemberGradeDisplayName gradeLimit;
}
