package com.example.circle.service;

import com.example.circle.application.dto.CreateMemberDto;
import com.example.circle.application.dto.UpdateMemberDto;
import com.example.circle.application.service.MemberCommandService;
import com.example.circle.domain.entity.Member;
import com.example.circle.domain.entity.MemberGrade;
import com.example.circle.domain.entity.MemberGradeDisplayName;
import com.example.circle.domain.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberCommandServiceTest {

    @Autowired
    MemberCommandService memberCommandService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 멤버_생성_테스트_성공() {
        // given
        String memberName = "멤버1";
        MemberGradeDisplayName memberGradeDisplayName = MemberGradeDisplayName.SILVER;
        String email = "aaa@naver.com";
        CreateMemberDto createMemberDto = new CreateMemberDto(memberName, memberGradeDisplayName, email);

        // when
        Long memberId = memberCommandService.createMember(createMemberDto);
        Member createdMember = memberRepository.findById(memberId).get();

        // then
        assertThat(createdMember.getUserName()).isEqualTo(memberName);
        assertThat(createdMember.getGrade().getDisplayName()).isEqualTo(memberGradeDisplayName);
    }

    @Test
    void 멤버_생성_테스트_실패_잘못된이메일형식() {
        // given
        String memberName = "멤버1";
        MemberGradeDisplayName memberGradeDisplayName = MemberGradeDisplayName.SILVER;
        String email = "aaanaver.com";
        CreateMemberDto createMemberDto = new CreateMemberDto(memberName, memberGradeDisplayName, email);

        // when & then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberCommandService.createMember(createMemberDto);
        });
    }

    @Test
    void 멤버_수정_테스트_성공() {
        // given
        String memberName = "멤버1";
        MemberGradeDisplayName memberGradeDisplayName = MemberGradeDisplayName.SILVER;
        MemberGrade grade = new MemberGrade(memberGradeDisplayName);
        String email = "aaa@naver.com";
        Member createdMember = memberRepository.save(new Member(memberName, grade, email));
        String changedMemberName = "수정된 멤버명";
        UpdateMemberDto updateMemberDto = new UpdateMemberDto(changedMemberName, memberGradeDisplayName, email);

        memberCommandService.updateMember(createdMember.getId(), updateMemberDto);

        Member updatedMember = memberRepository.findById(createdMember.getId()).get();
        assertThat(updatedMember.getUserName()).isEqualTo(changedMemberName);
    }

    @Test
    void 멤버_수정_테스트_실패_잘못된이메일() {
        // given
        String memberName = "멤버1";
        MemberGradeDisplayName memberGradeDisplayName = MemberGradeDisplayName.SILVER;
        MemberGrade grade = new MemberGrade(memberGradeDisplayName);
        String email = "aaa@naver.com";
        Member createdMember = memberRepository.save(new Member(memberName, grade, email));
        String changedWrongEmail = "asdfasdfasdf";
        UpdateMemberDto updateMemberDto = new UpdateMemberDto(memberName, memberGradeDisplayName, changedWrongEmail);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberCommandService.updateMember(createdMember.getId(), updateMemberDto);
        });
    }
}
