package com.example.circle.domain.repository;


import com.example.circle.domain.entity.Circle;
import com.example.circle.domain.entity.CircleMember;
import com.example.circle.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircleMemberRepository extends JpaRepository<CircleMember, Long> {
    public CircleMember findByCircle(Circle circle);
    public CircleMember findByMember(Member member);
}
