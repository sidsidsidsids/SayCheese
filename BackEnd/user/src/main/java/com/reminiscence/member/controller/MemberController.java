package com.reminiscence.member.controller;

//import com.reminiscence.JwtServiceImpl;

import com.reminiscence.config.auth.MemberDetail;
import com.reminiscence.domain.Member;
import com.reminiscence.member.dto.*;
import com.reminiscence.member.service.MemberService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

//    @Autowired
//    private JwtServiceImpl jwtService;

    @Autowired
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        super();
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public ResponseEntity join(@Valid @RequestBody MemberJoinRequestDto requestDto, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        log.debug("MemberJoinRequestDto info : {}", requestDto);
        return memberService.joinMember(requestDto);
    }

    @GetMapping("/join/{email}/id-check")
    public ResponseEntity idCheck(@PathVariable("email") String email) throws Exception {
        log.debug("idCheck email : {}", email);
        Member member = memberService.emailCheck(email);
        if (member != null) {
            return ResponseEntity.ok("이미 사용중인 아이디입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        }
    }

    @GetMapping("/join/{nickname}/nickname-check")
    @ResponseBody
    public ResponseEntity nicknameCheck(@PathVariable("nickname") String nickname) throws Exception {
        log.debug("nicknameCheck nickname : {}", nickname);
        Member member = memberService.nicknameCheck(nickname);
        if (member != null) {
            return ResponseEntity.ok("이미 사용중인 닉네임입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 닉네임입니다.");
        }
    }

    @PutMapping("/modify")
    public ResponseEntity modify(@AuthenticationPrincipal MemberDetail memberDetail, @Valid @RequestBody MemberInfoUpdateRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        Member member;
        System.out.println(memberDetail);
        try {
            member = memberService.updateMemberInfo(memberDetail, requestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @GetMapping("/find-password/{memberId}")
    public Member findPassword(@PathVariable("memberId") String memberId) throws Exception {
        Member member = memberService.getMemberInfo(memberId);
        return member;
    }

    @PutMapping("/modify-password")
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
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity getInfo(@AuthenticationPrincipal MemberDetail memberDetail) throws Exception {
        String memberId = memberDetail.getMember().getEmail();
        Member member = memberService.getMemberInfo(memberId);
        return new ResponseEntity(member, HttpStatus.OK);
    }

//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> login(
//            @RequestBody
//            MemberLoginRequestDto requestDto) {
//        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = null;
//        try {
//            Member loginMember = memberService.login(requestDto);
//            if (loginMember != null) {
//                String accessToken = jwtService.createAccessToken("memberId", loginMember.getEmail());// key, data
//                String refreshToken = jwtService.createRefreshToken("memberId", loginMember.getEmail());// key, data
//                memberService.saveRefreshToken(requestDto.getEmail(), refreshToken);
//                logger.debug("로그인 accessToken 정보 : {}", accessToken);
//                logger.debug("로그인 refreshToken 정보 : {}", refreshToken);
//                resultMap.put("access-token", accessToken);
//                resultMap.put("refresh-token", refreshToken);
//                resultMap.put("message", SUCCESS);
//                status = HttpStatus.ACCEPTED;
//            } else {
//                resultMap.put("message", FAIL);
//                status = HttpStatus.ACCEPTED;
//            }
//        } catch (Exception e) {
//            logger.error("로그인 실패 : {}", e);
//            resultMap.put("message", e.getMessage());
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return new ResponseEntity<Map<String, Object>>(resultMap, status);
//    }
//
//    @GetMapping("/info/{memberId}")
//    public ResponseEntity<Map<String, Object>> getInfo(
//            @PathVariable("memberId")
//            String memberId,
//            HttpServletRequest request) {
////		logger.debug("memberId : {} ", memberId);
//        Map<String, Object> resultMap = new HashMap<>();
////        HttpStatus status = HttpStatus.ACCEPTED;
//        HttpStatus status = HttpStatus.UNAUTHORIZED;
//        if (jwtService.isUsable(request.getHeader("access-token"))) {
//            logger.info("사용 가능한 토큰!!!");
//            try {
////				로그인 사용자 정보.
//                Member member = memberService.getMemberInfo(memberId);
//                resultMap.put("MemberInfo", member);
//                resultMap.put("message", SUCCESS);
//                status = HttpStatus.ACCEPTED;
//            } catch (Exception e) {
//                logger.error("정보조회 실패 : {}", e);
//                resultMap.put("message", e.getMessage());
//                status = HttpStatus.INTERNAL_SERVER_ERROR;
//            }
//        } else {
//            logger.error("사용 불가능 토큰!!!");
//            resultMap.put("message", FAIL);
//            status = HttpStatus.ACCEPTED;
//        }
//        return new ResponseEntity<Map<String, Object>>(resultMap, status);
//    }
//
//    @GetMapping("/logout/{memberId}")
//    public ResponseEntity<?> removeToken(@PathVariable("memberId") String memberId) {
//        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = HttpStatus.ACCEPTED;
//        try {
//            memberService.deleteRefreshToken(memberId);
//            resultMap.put("message", SUCCESS);
//            status = HttpStatus.ACCEPTED;
//        } catch (Exception e) {
//            logger.error("로그아웃 실패 : {}", e);
//            resultMap.put("message", e.getMessage());
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return new ResponseEntity<Map<String, Object>>(resultMap, status);
//    }
//
//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(@RequestBody Member member, HttpServletRequest request)
//            throws Exception {
//        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = HttpStatus.ACCEPTED;
//        String token = request.getHeader("refresh-token");
//        logger.debug("token : {}, member : {}", token, member);
//        if (jwtService.isUsable(token)) {
//            if (token.equals(memberService.getRefreshToken(String.valueOf(member.getId())))) {
//                String accessToken = jwtService.createAccessToken("memberId", member.getId());
//                logger.debug("token : {}", accessToken);
//                logger.debug("정상적으로 액세스토큰 재발급!!!");
//                resultMap.put("access-token", accessToken);
//                resultMap.put("message", SUCCESS);
//                status = HttpStatus.ACCEPTED;
//            }
//        } else {
//            logger.debug("리프레쉬토큰도 사용불가!!!!!!!");
//            status = HttpStatus.UNAUTHORIZED;
//        }
//        return new ResponseEntity<Map<String, Object>>(resultMap, status);
//    }
}

