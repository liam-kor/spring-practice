package com.example.circle.application.service;

import com.example.circle.application.dto.CircleMemberJoinDto;
import com.example.circle.application.dto.CreateCircleDto;
import com.example.circle.domain.entity.Circle;
import com.example.circle.domain.entity.CircleMember;
import com.example.circle.domain.entity.Member;
import com.example.circle.domain.entity.MemberGrade;
import com.example.circle.domain.repository.CircleMemberRepository;
import com.example.circle.domain.repository.CircleRepository;
import com.example.circle.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CircleCommandService {
    private final CircleRepository circleRepository;
    private final MemberRepository memberRepository;

    private final CircleMemberRepository circleMemberRepository;

    public void joinMember(CircleMemberJoinDto input) {
        Circle circle = circleRepository.findById(input.getCircleId()).orElseThrow(() -> new IllegalStateException("존재하지 않는 서클입니다."));
        Member member = memberRepository.findById(input.getMemberId()).orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        CircleMember isAlreadyJoined = circleMemberRepository.findByCircle(circle);
        if (isAlreadyJoined != null) {
            throw new IllegalStateException("이미 가입된 서클입니다.");
        }
        if (!circle.isMemberSatisfiedGradeLimit(member.getGrade())) {
            throw new IllegalStateException("가입 등급이 맞지 않습니다.");
        } else {
            System.out.println("가입 등급이 적절합니다.");
        }
        CircleMember circleMember = new CircleMember(circle, member);
        circleMemberRepository.save(circleMember);
    }

    @Transactional
    public Long createCircle(CreateCircleDto input) {
        Member owner = memberRepository.findById(input.getOwnerId()).orElseThrow(() -> new IllegalStateException("존재하지 않는 유저 아이디"));
        Circle isExists = circleRepository.findByCircleName(input.getCircleName());
        if (isExists != null) {
            throw new IllegalStateException("이미 존재하는 서클명입니다.");
        }
        Circle circle = new Circle(input.getCircleName(), owner, new MemberGrade(input.getGradeLimit()));
        Circle createdCircle = circleRepository.save(circle);
        return createdCircle.getId();
    }
}
