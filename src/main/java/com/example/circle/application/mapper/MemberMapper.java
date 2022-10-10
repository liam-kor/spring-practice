package com.example.circle.application.mapper;

import com.example.circle.application.dto.MemberDto;
import com.example.circle.domain.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberDto toDto(Member member);
}
