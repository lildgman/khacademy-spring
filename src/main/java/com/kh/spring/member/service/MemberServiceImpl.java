package com.kh.spring.member.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;

//component 보다 더 구체적으로 @Service bean으로 등록시킬 수 있다.
@Service 
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private SqlSessionTemplate sqlSession; // 기존 myBatis SqlSession 객체 대체
	
	@Override
	public Member loginMember(Member member) {
		//SqlSessionTemplate bean 등록 후 @Autowired 하였다.
		//스프링이 사용 후 자동으로 반납시켜주기 때문에 close 메소드로 자원을 반납할 필요가 없다.
		//한줄로 기술이 가능하다.
		return memberDao.loginMember(sqlSession, member);
	}

	@Override
	public int idCheck(String checkId) {
		return memberDao.idCheck(sqlSession, checkId);
	}

	@Override
	public int insertMember(Member member) {
		return memberDao.insertMember(sqlSession, member);
	}

	@Override
	public int updateMember(Member member) {
		return memberDao.updateMember(sqlSession, member);
	}

	@Override
	public int deleteMember(String userId) {	
		return memberDao.deleteMember(sqlSession, userId);
	}

	
	

}
