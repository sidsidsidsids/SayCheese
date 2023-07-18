package com.reminiscence.controller.member;

import com.reminiscence.model.member.Member;
import com.reminiscence.service.member.MemberService;
import com.reminiscence.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private MemberService MemberService;

    public MemberController(MemberService MemberService) {
        super();
        this.MemberService = MemberService;
    }

    @PutMapping("/modify")
    public String modify(Member Member) {
        //Todo
        return null;
    }

    @GetMapping("/find-password/{Memberid}")
    public Member findPassword(@PathVariable("Memberid") String Memberid) throws Exception {
        Member Member = MemberService.memberInfo(Memberid);
        return Member;
    }

    @PutMapping("/modify-password")
    public void modifyPassword(@RequestBody Member Member) throws Exception {
        MemberService.updateMember(Member);
    }

    @GetMapping("/regist")
    public String join() {
        return "Member/join";
    }

    @GetMapping("/regist/{Memberid}/id-check")
    @ResponseBody
    public String idCheck(@PathVariable("Memberid") String MemberId) throws Exception {
        logger.debug("idCheck Memberid : {}", MemberId);
        int cnt = MemberService.idCheck(MemberId);
        return cnt + "";
    }

    @PostMapping("/regist")
    public void join(@RequestBody Member Member, Model model) {
        logger.debug("memberDto info : {}", Member);
        try {
            MemberService.join(Member);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/login")
    public String login() {
        System.out.println("login");
        return "member/login";
    }

//    @ApiOperation(value = "로그인", notes = "Access-token과 로그인 결과 메세지를 반환한다.", response = Map.class)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody
//            @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true)
            Member Member) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            Member loginMember = MemberService.login(Member);
            if (loginMember != null) {
                String accessToken = jwtService.createAccessToken("Memberid", loginMember.getId());// key, data
                String refreshToken = jwtService.createRefreshToken("Memberid", loginMember.getId());// key, data
                MemberService.saveRefreshToken(String.valueOf(Member.getId()), refreshToken);
                logger.debug("로그인 accessToken 정보 : {}", accessToken);
                logger.debug("로그인 refreshToken 정보 : {}", refreshToken);
                resultMap.put("access-token", accessToken);
                resultMap.put("refresh-token", refreshToken);
                resultMap.put("message", SUCCESS);
                status = HttpStatus.ACCEPTED;
            } else {
                resultMap.put("message", FAIL);
                status = HttpStatus.ACCEPTED;
            }
        } catch (Exception e) {
            logger.error("로그인 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

//    @ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
    @GetMapping("/info/{Memberid}")
    public ResponseEntity<Map<String, Object>> getInfo(
            @PathVariable("Memberid")
//            @ApiParam(value = "인증할 회원의 아이디.", required = true)
            String Memberid,
            HttpServletRequest request) {
//		logger.debug("Memberid : {} ", Memberid);
        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = HttpStatus.ACCEPTED;
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        if (jwtService.isUsable(request.getHeader("access-token"))) {
            logger.info("사용 가능한 토큰!!!");
            try {
//				로그인 사용자 정보.
                Member Member = MemberService.memberInfo(Memberid);
                resultMap.put("MemberInfo", Member);
                resultMap.put("message", SUCCESS);
                status = HttpStatus.ACCEPTED;
            } catch (Exception e) {
                logger.error("정보조회 실패 : {}", e);
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            logger.error("사용 불가능 토큰!!!");
            resultMap.put("message", FAIL);
            status = HttpStatus.ACCEPTED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


    //    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "redirect:/";
//    }
//    @ApiOperation(value = "로그아웃", notes = "회원 정보를 담은 Token을 제거한다.", response = Map.class)
    @GetMapping("/logout/{Memberid}")
    public ResponseEntity<?> removeToken(@PathVariable("Memberid") String Memberid) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            MemberService.deleRefreshToken(Memberid);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            logger.error("로그아웃 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

//    @ApiOperation(value = "Access Token 재발급", notes = "만료된 access token을 재발급받는다.", response = Map.class)
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Member Member, HttpServletRequest request)
            throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        String token = request.getHeader("refresh-token");
        logger.debug("token : {}, memberDto : {}", token, Member);
        if (jwtService.isUsable(token)) {
            if (token.equals(MemberService.getRefreshToken(String.valueOf(Member.getId())))) {
                String accessToken = jwtService.createAccessToken("Memberid", Member.getId());
                logger.debug("token : {}", accessToken);
                logger.debug("정상적으로 액세스토큰 재발급!!!");
                resultMap.put("access-token", accessToken);
                resultMap.put("message", SUCCESS);
                status = HttpStatus.ACCEPTED;
            }
        } else {
            logger.debug("리프레쉬토큰도 사용불!!!!!!!");
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


    @GetMapping("/list")
    public String list() {
        return "redirect:/assets/list.html";
    }

}

