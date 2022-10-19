package com.example.circle.domain.repository;

import com.example.circle.domain.entity.CircleEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircleEventRepository extends JpaRepository<CircleEvent, Long> {
}
