package com.example.circle.application.service;

import com.example.circle.domain.dto.CreateMemberDto;
import com.example.circle.domain.dto.UpdateMemberDto;
import com.example.circle.domain.entity.Member;
import com.example.circle.domain.entity.MemberGrade;
import com.example.circle.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberDomainService {
    private final MemberRepository memberRepository;
    @Transactional
    public Long createMember(CreateMemberDto input) {
        Member member = new Member(input.getMemberName(),
                new MemberGrade(input.getMemberGradeDisplayName()),
                input.getEmail());
        Member createdMember = memberRepository.save(member);
        return createdMember.getId();
    }

    @Transactional
    public void updateMember(Long id, UpdateMemberDto updateMemberDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저 아이디"));
        member.update(
                updateMemberDto.getUserName(),
                new MemberGrade(updateMemberDto.getGradeDisplayName()),
                updateMemberDto.getEmail()
        );
    }
}
