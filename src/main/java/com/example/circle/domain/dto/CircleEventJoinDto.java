package com.example.circle.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CircleEventJoinDto {
    private Long memberId;
    private Long circleEventId;
}
