package com.example.circle.application.service;

import com.example.circle.application.dto.MemberDto;
import com.example.circle.application.mapper.MemberMapper;
import com.example.circle.domain.entity.Member;
import com.example.circle.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberQueryService {
    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;

    public MemberDto getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원 아이디"));
        return memberMapper.toDto(member);
    }
}
