package com.reminiscence.service.member;

import java.util.HashMap;
import java.util.Map;

import com.reminiscence.repository.member.MemberRepository;
import com.reminiscence.model.member.Member;
import org.springframework.stereotype.Service;


@Service
public class MemberServiceImpl implements MemberService {

//	@Autowired
//	private SqlSession sqlSession;

    private MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        super();
        this.memberRepository = memberRepository;
    }

    @Override
    public int idCheck(String memberId) throws Exception {
//		return sqlSession.getMapper(MemberMapper.class).idCheck(memberId);
        return memberRepository.idCheck(memberId);
    }

    @Override
    public void join(Member member) throws Exception {
//		sqlSession.getMapper(MemberMapper.class).joinMember(memberDto);
        memberRepository.join(member);
    }

    //	@Override
//	public MemberDto login(Map<String, String> map) throws Exception {
////		return sqlSession.getMapper(MemberMapper.class).loginMember(map);
//		return memberDao.login(map);
//	}
    @Override
    public Member login(Member member) throws Exception {
        if(member.getId() == null || member.getPassword() == null)
            return null;
        return memberRepository.login(member);
//	return sqlSession.getMapper(MemberMapper.class).login(memberDto);
    }

    @Override
    public void updatePw(Map<String, String> map) throws Exception {
        memberRepository.updatePw(map);
    }

//	/* ADMIN */
//	@Override
//	public List<MemberDto> listMember(Map<String, Object> map) throws Exception {
//		return memberDao.listMember(map);
//	}

//	@Override
//	public MemberDto getMember(String memberId) throws Exception {
//		return memberDao.getMemberInfo(memberId);
//	}

    @Override
    public Member memberInfo(String memberid) throws Exception {
        return memberRepository.memberInfo(memberid);
//		return sqlSession.getMapper(MemberMapper.class).memberInfo(memberid);
    }

    @Override
    public void updateMember(Member member) throws Exception {
        memberRepository.modify(member);
    }

    @Override
    public void deleteMember(String memberId) throws Exception {
        memberRepository.delete(memberId);
    }

    @Override
    public void saveRefreshToken(String memberid, String refreshToken) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("memberid", memberid);
        map.put("token", refreshToken);
        memberRepository.saveRefreshToken(map);
    }

    @Override
    public Object getRefreshToken(String memberid) throws Exception {
        return memberRepository.getRefreshToken(memberid);
    }

    @Override
    public void deleRefreshToken(String memberid) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("memberid", memberid);
        map.put("token", null);
        memberRepository.deleteRefreshToken(map);
    }

}
