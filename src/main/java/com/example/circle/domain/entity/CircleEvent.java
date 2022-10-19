package com.example.circle.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Setter(value = AccessLevel.PRIVATE)
public class CircleEvent {
    @Id @GeneratedValue
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedDate
    private LocalDateTime startedAt;

    private String title;

    private String description;

    private String location;

    private int maxJoinMemberLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "circle_id")
    private Circle circle;

    @OneToMany(mappedBy = "circleEvent")
    private List<CircleEventMember> joinMembers = new ArrayList<>();

    public CircleEvent(String title, String description, String location, LocalDateTime startedAt, int maxJoinMemberLimit, Circle circle) {
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setMaxJoinMemberLimit(maxJoinMemberLimit);
        setCircle(circle);
    }
}
