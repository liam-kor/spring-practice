package com.example.circle.domain.service;

import com.example.circle.domain.dto.CircleEventJoinDto;
import com.example.circle.domain.dto.CircleMemberJoinDto;
import com.example.circle.domain.dto.CreateCircleDto;
import com.example.circle.domain.dto.CreateCircleEventDto;
import com.example.circle.domain.entity.*;
import com.example.circle.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CircleDomainService {
    private final CircleRepository circleRepository;
    private final MemberRepository memberRepository;

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
