package com.reminiscence.member.controller;

//import com.reminiscence.JwtServiceImpl;

import com.reminiscence.config.auth.MemberDetail;
import com.reminiscence.config.redis.RedisKey;
import com.reminiscence.domain.Member;
import com.reminiscence.exception.customexception.EmailException;
import com.reminiscence.exception.message.EmailExceptionMessage;
import com.reminiscence.member.dto.*;
import com.reminiscence.member.service.MemberService;
import com.reminiscence.message.Response;
import com.reminiscence.message.custom_message.MemberResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.reminiscence.filter.JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    private final RedisTemplate redisTemplate;

    private final MemberService memberService;



    @PostMapping("/join")
    public ResponseEntity join(@Valid @RequestBody MemberJoinRequestDto requestDto) throws Exception {
        log.debug("MemberJoinRequestDto info : {}", requestDto);
        String token=(String)redisTemplate.opsForValue().get(RedisKey.EMAIL_AUTH_SUCCESS_TOKEN_PREFIX+requestDto.getEmail());
        if(token==null){
            throw new EmailException(EmailExceptionMessage.DATA_NOT_FOUND);
        }
        memberService.joinMember(requestDto);
        return new ResponseEntity<>(Response.of(MemberResponseMessage.MEMBER_JOIN_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/join/{email}/id-check")
    public ResponseEntity idCheck(@PathVariable("email") String email) throws Exception {
        log.debug("idCheck email : {}", email);
        Member member = memberService.emailCheck(email);
        if (member != null) {
            return new ResponseEntity<>(Response.of(MemberResponseMessage.MEMBER_ID_CHECK_FAILURE), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(Response.of(MemberResponseMessage.MEMBER_ID_CHECK_SUCCESS), HttpStatus.OK);
        }
    }

    @GetMapping("/join/{nickname}/nickname-check")
    @ResponseBody
    public ResponseEntity nicknameCheck(@PathVariable("nickname") String nickname) throws Exception {
        log.debug("nicknameCheck nickname : {}", nickname);
        Member member = memberService.nicknameCheck(nickname);
        if (member != null) {
            return new ResponseEntity<>(Response.of(MemberResponseMessage.MEMBER_NICKNAME_CHECK_FAILURE), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(Response.of(MemberResponseMessage.MEMBER_NICKNAME_CHECK_SUCCESS), HttpStatus.OK);
        }
    }

    @PutMapping("/modify")
    public ResponseEntity modify(@AuthenticationPrincipal MemberDetail memberDetail, @Valid @RequestBody MemberInfoUpdateRequestDto requestDto, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }
        return memberService.updateMemberInfo(memberDetail, requestDto);
    }

    @PostMapping("/password")
    public Member findPassword(@RequestBody MemberFindPasswordDto memberFindPasswordDto) throws Exception {
        Member member = memberService.emailCheck(memberFindPasswordDto.getEmail());
        return member;
    }

    @PutMapping("/password")
    public void modifyPassword(@RequestBody MemberUpdatePasswordRequestDto memberUpdatePasswordRequestDto) throws Exception {
        memberService.updateMemberPassword(memberUpdatePasswordRequestDto);
    }

    @GetMapping("/search-member/{email-nickname}")
    public ResponseEntity findMembers(@PathVariable("email-nickname") String emailNickname) throws Exception {
        List<MemberSearchResponseDto> memberList = memberService.getMemberList(emailNickname);
        return new ResponseEntity<>(memberList, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@AuthenticationPrincipal MemberDetail memberDetail) throws Exception {
        long memberId = memberDetail.getMember().getId();
        memberService.deleteMember(memberId);
        return new ResponseEntity<>(Response.of(MemberResponseMessage.MEMBER_DELETE_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponseDto> getInfo(@AuthenticationPrincipal MemberDetail memberDetail) throws Exception {
        String memberId = memberDetail.getMember().getEmail();
        MemberInfoResponseDto memberInfoResponseDto = memberService.getMemberInfo(memberId);
        return new ResponseEntity<>(memberInfoResponseDto, HttpStatus.OK);
    }

    @GetMapping("/nickname")
    public ResponseEntity getNickname(@AuthenticationPrincipal MemberDetail memberDetail) throws Exception {
        MemberNicknameResponseDto memberNicknameResponseDto = memberService.getMemberNickName(memberDetail);
        return new ResponseEntity<>(memberNicknameResponseDto, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity saveProfile(@AuthenticationPrincipal MemberDetail memberDetail,
                                                   @RequestBody @Valid MemberProfileSaveRequestDto requestDto) {
        return new ResponseEntity<>(memberService.saveProfile(memberDetail, requestDto),HttpStatus.OK);
    }

}

