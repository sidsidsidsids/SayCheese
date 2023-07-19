package com.reminiscence.member.controller;

import com.reminiscence.JwtServiceImpl;
import com.reminiscence.domain.Member;
import com.reminiscence.domain.MemberJoinRequestDto;
import com.reminiscence.domain.MemberUpdateRequestDto;
import com.reminiscence.service.member.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        super();
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public void join(@RequestBody MemberJoinRequestDto requestDto) {
        logger.debug("memberDto info : {}", requestDto);
        try {
            memberService.joinMember(requestDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @PutMapping("/modify")
    public void modify(@RequestBody MemberUpdateRequestDto requestDto)  {
        try {
            memberService.updateMemberInfo(requestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/modify-password/{memberid}")
    public Member modifyPassword(@PathVariable("memberid") String memberid) throws Exception {
        Member member = memberService.memberInfo(memberid);
        return member;
    }
    @PutMapping("/find-password/{memberid}")
    public Member findPassword(@PathVariable("memberid") String memberid) throws Exception {
        Member member = memberService.memberInfo(memberid);
        return member;
    }
//
//    @GetMapping("/find-password/{memberid}")
//    public Member findPassword(@PathVariable("memberid") String memberid) throws Exception {
//        Member member = memberService.memberInfo(memberid);
//        return member;
//    }
//
//    @PutMapping("/modify-password")
//    public void modifyPassword(@RequestBody Member member) throws Exception {
//        memberService.updateMember(member);
//    }
//
//    @GetMapping("/regist")
//    public String join() {
//        return "member/join";
//    }
//
//    @GetMapping("/regist/{memberid}/id-check")
//    @ResponseBody
//    public String idCheck(@PathVariable("memberid") String memberId) throws Exception {
//        logger.debug("idCheck memberid : {}", memberId);
//        int cnt = memberService.idCheck(memberId);
//        return cnt + "";
//    }
//
//    @GetMapping("/login")
//    public String login() {
//        System.out.println("login");
//        return "member/login";
//    }
//
////    @ApiOperation(value = "로그인", notes = "Access-token과 로그인 결과 메세지를 반환한다.", response = Map.class)
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> login(
//            @RequestBody
////            @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true)
//            Member member) {
//        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = null;
//        try {
//            Member loginMember = memberService.login(member);
//            if (loginMember != null) {
//                String accessToken = jwtService.createAccessToken("memberid", loginMember.getId());// key, data
//                String refreshToken = jwtService.createRefreshToken("memberid", loginMember.getId());// key, data
//                memberService.saveRefreshToken(String.valueOf(member.getId()), refreshToken);
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
////    @ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
//    @GetMapping("/info/{memberid}")
//    public ResponseEntity<Map<String, Object>> getInfo(
//            @PathVariable("memberid")
////            @ApiParam(value = "인증할 회원의 아이디.", required = true)
//            String memberid,
//            HttpServletRequest request) {
////		logger.debug("memberid : {} ", memberid);
//        Map<String, Object> resultMap = new HashMap<>();
////        HttpStatus status = HttpStatus.ACCEPTED;
//        HttpStatus status = HttpStatus.UNAUTHORIZED;
//        if (jwtService.isUsable(request.getHeader("access-token"))) {
//            logger.info("사용 가능한 토큰!!!");
//            try {
////				로그인 사용자 정보.
//                Member member = memberService.memberInfo(memberid);
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
//
//    //    @GetMapping("/logout")
////    public String logout(HttpSession session) {
////        session.invalidate();
////        return "redirect:/";
////    }
////    @ApiOperation(value = "로그아웃", notes = "회원 정보를 담은 Token을 제거한다.", response = Map.class)
//    @GetMapping("/logout/{memberid}")
//    public ResponseEntity<?> removeToken(@PathVariable("memberid") String memberid) {
//        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = HttpStatus.ACCEPTED;
//        try {
//            memberService.deleRefreshToken(memberid);
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
////    @ApiOperation(value = "Access Token 재발급", notes = "만료된 access token을 재발급받는다.", response = Map.class)
//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(@RequestBody Member member, HttpServletRequest request)
//            throws Exception {
//        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = HttpStatus.ACCEPTED;
//        String token = request.getHeader("refresh-token");
//        logger.debug("token : {}, memberDto : {}", token, member);
//        if (jwtService.isUsable(token)) {
//            if (token.equals(memberService.getRefreshToken(String.valueOf(member.getId())))) {
//                String accessToken = jwtService.createAccessToken("memberid", member.getId());
//                logger.debug("token : {}", accessToken);
//                logger.debug("정상적으로 액세스토큰 재발급!!!");
//                resultMap.put("access-token", accessToken);
//                resultMap.put("message", SUCCESS);
//                status = HttpStatus.ACCEPTED;
//            }
//        } else {
//            logger.debug("리프레쉬토큰도 사용불!!!!!!!");
//            status = HttpStatus.UNAUTHORIZED;
//        }
//        return new ResponseEntity<Map<String, Object>>(resultMap, status);
//    }
//
//
//    @GetMapping("/list")
//    public String list() {
//        return "redirect:/assets/list.html";
//    }

}

