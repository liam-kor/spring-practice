package com.example.circle.application.controller;

import com.example.circle.application.dto.CreateCircleDto;
import com.example.circle.application.dto.CreateMemberDto;
import com.example.circle.application.dto.MemberDto;
import com.example.circle.application.service.CircleCommandService;
import com.example.circle.application.service.MemberCommandService;
import com.example.circle.application.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CircleAppController {

    private final CircleCommandService circleCommandService;
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @PostMapping("/apis/circles")
    public Long createCircle(CreateCircleDto input) {
        return circleCommandService.createCircle(input);
    }

    @PostMapping("/apis/members")
    public Long createMember(@RequestBody CreateMemberDto request) {
        return memberCommandService.createMember(request);
    }

    @GetMapping("/apis/members/{id}")
    public MemberDto getMember(@PathVariable("id") Long id) {
        return memberQueryService.getMember(id);
    }

//    @PostMapping("/apis/join")
//    public void joinMember() {
//        circleCommandService.joinMember(1l, 2l);
//    }

//    @Transactional
//    @GetMapping("/apis/circle-members")
//    public List<MemberDto> getCircleMembers() {
//        return circleCommandService.getCircleMembers(1l);
//    }
}
