package com.reminiscence.member.service;

import com.reminiscence.domain.Member;
import com.reminiscence.domain.MemberJoinRequestDto;
import com.reminiscence.domain.MemberUpdatePasswordDto;
import com.reminiscence.domain.MemberUpdateRequestDto;
import com.reminiscence.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        super();
        this.memberRepository = memberRepository;
    }

    @Override
    public Member joinMember(MemberJoinRequestDto memberJoinRequestDto) throws Exception {
        Member member = memberJoinRequestDto.toEntity();
        return memberRepository.save(member);
    }

    @Override
    public Member updateMemberInfo(MemberUpdateRequestDto memberUpdateRequestDto) throws Exception {
        Member member = memberUpdateRequestDto.toEntity();
        return memberRepository.save(member);
    }

    @Override
	public Member getMemberInfo(long memberId) throws Exception {
        Member member = memberRepository.findById(memberId).get();
        return member;
	}

	@Override
	public List<Member> getMemberList(String key) throws Exception {
        List<Member> memberlist = memberRepository.getMemberList(key);
		return memberlist;
	}

    @Override
    public Member updateMemberPassword(MemberUpdatePasswordDto memberUpdatePasswordDto) throws Exception {
        Member member = memberUpdatePasswordDto.toEntity();
        return memberRepository.save(member);
    }

//    @Override
//    public int idCheck(String memberId) throws Exception {
////		return sqlSession.getMapper(MemberMapper.class).idCheck(memberId);
//        return memberRepository.idCheck(memberId);
//    }
//
//
//    //	@Override
////	public MemberDto login(Map<String, String> map) throws Exception {
//////		return sqlSession.getMapper(MemberMapper.class).loginMember(map);
////		return memberDao.login(map);
////	}
//    @Override
//    public Member login(Member member) throws Exception {
//        if(member.getId() == null || member.getPassword() == null)
//            return null;
//        return memberRepository.login(member);
////	return sqlSession.getMapper(MemberMapper.class).login(memberDto);
//    }
//
//
////	/* ADMIN */
//
//
//    @Override
//    public Member memberInfo(String memberid) throws Exception {
//        return memberRepository.memberInfo(memberid);
////		return sqlSession.getMapper(MemberMapper.class).memberInfo(memberid);
//    }
//
//
//    @Override
//    public void deleteMember(String memberId) throws Exception {
//        memberRepository.delete(memberId);
//    }
//
//    @Override
//    public void saveRefreshToken(String memberid, String refreshToken) throws Exception {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("memberid", memberid);
//        map.put("token", refreshToken);
//        memberRepository.saveRefreshToken(map);
//    }
//
//    @Override
//    public Object getRefreshToken(String memberid) throws Exception {
//        return memberRepository.getRefreshToken(memberid);
//    }
//
//    @Override
//    public void deleRefreshToken(String memberid) throws Exception {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("memberid", memberid);
//        map.put("token", null);
//        memberRepository.deleteRefreshToken(map);
//    }

}
