package com.example.circle.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CircleMemberJoinDto {
    private Long circleId;
    private Long memberId;
}
