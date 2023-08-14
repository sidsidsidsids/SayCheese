package com.reminiscence.member.controller;

//import com.reminiscence.JwtServiceImpl;

import com.reminiscence.config.auth.MemberDetail;
import com.reminiscence.config.redis.RedisKey;
import com.reminiscence.domain.Member;
import com.reminiscence.email.EmailType;
import com.reminiscence.email.dto.EmailRequestDto;
import com.reminiscence.email.service.EmailService;
import com.reminiscence.exception.customexception.EmailException;
import com.reminiscence.exception.customexception.MemberException;
import com.reminiscence.exception.message.EmailExceptionMessage;
import com.reminiscence.exception.message.MemberExceptionMessage;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    private final RedisTemplate redisTemplate;

    private final MemberService memberService;

    private final EmailService emailService;

    @PostMapping("/join")
    public ResponseEntity join(@Valid @RequestBody MemberJoinRequestDto requestDto) throws Exception {
        log.debug("MemberJoinRequestDto info : {}", requestDto);
        String token = (String) redisTemplate.opsForValue().get(RedisKey.EMAIL_AUTH_SUCCESS_TOKEN_PREFIX + requestDto.getEmail());
        if (token == null) {
            throw new EmailException(EmailExceptionMessage.DATA_NOT_FOUND);
        }
        memberService.joinMember(requestDto);
        return new ResponseEntity<>(Response.of(MemberResponseMessage.MEMBER_JOIN_SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/id-check")
    public ResponseEntity idCheck(@RequestBody MemberIdCheckRequestDto memberIdCheckRequestDto) throws Exception {
        log.debug("idCheck email : {}", memberIdCheckRequestDto.getEmail());
        Member member = memberService.emailCheck(memberIdCheckRequestDto.getEmail());
        if (member != null) {
            return new ResponseEntity<>(Response.of(MemberResponseMessage.MEMBER_ID_CHECK_FAILURE), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(Response.of(MemberResponseMessage.MEMBER_ID_CHECK_SUCCESS), HttpStatus.OK);
        }
    }

    @PostMapping("/nickname-check")
    public ResponseEntity nicknameCheck(@RequestBody MemberNicknameCheckRequestDto memberNicknameCheckRequestDto) throws Exception {
        log.debug("nicknameCheck nickname : {}", memberNicknameCheckRequestDto.getNickname());
        Member member = memberService.nicknameCheck(memberNicknameCheckRequestDto.getNickname());
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
    public ResponseEntity findPassword(@RequestBody MemberFindPasswordRequestDto memberFindPasswordRequestDto) throws Exception {
        Member member = memberService.emailCheck(memberFindPasswordRequestDto.getEmail());
        if (member == null)
            return new ResponseEntity(new MemberFindPasswordResponseDto(MemberResponseMessage.MEMBER_EMAIL_CHECK_REQUEST), HttpStatus.BAD_REQUEST);
        emailService.storeAuthToken(new EmailRequestDto(member.getEmail()), EmailType.FIND_PW);
        return new ResponseEntity(new MemberFindPasswordResponseDto(MemberResponseMessage.MEMBER_PASSWORD_FIND_EMAIL_SEND_SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity modifyPassword(@RequestBody MemberUpdatePasswordRequestDto memberUpdatePasswordRequestDto) throws Exception {
        memberService.updateMemberPassword(memberUpdatePasswordRequestDto);
        return new ResponseEntity(Response.of(MemberResponseMessage.MEMBER_PASSWORD_MODIFY_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/search-member")
    public ResponseEntity findMembers(@RequestParam(value = "email-nickname", required = false, defaultValue = "") String emailNickname) throws Exception {
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
        return new ResponseEntity<>(memberService.saveProfile(memberDetail, requestDto), HttpStatus.OK);
    }

}

