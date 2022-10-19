package com.example.circle.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateCircleEventDto {
    private Long circleId;
    private String circleEventTitle;
    private String circleEventDescription;
    private String circleEventLocation;
    private LocalDateTime startedAt;
    private int maxJoinMemberLimit;
}
