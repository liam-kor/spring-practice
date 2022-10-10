package com.example.circle.domain.repository;

import com.example.circle.domain.entity.Circle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircleRepository extends JpaRepository<Circle, Long> {
    Circle findByCircleName(String circleName);
}
